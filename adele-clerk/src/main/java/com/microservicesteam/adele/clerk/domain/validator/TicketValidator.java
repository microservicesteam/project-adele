package com.microservicesteam.adele.clerk.domain.validator;

import static com.microservicesteam.adele.clerk.domain.validator.ValidationResult.INVALID_NO_TICKET;
import static com.microservicesteam.adele.clerk.domain.validator.ValidationResult.INVALID_TICKETS_OUT_OF_SECTOR;
import static com.microservicesteam.adele.clerk.domain.validator.ValidationResult.INVALID_TICKETS_RESERVED;
import static com.microservicesteam.adele.clerk.domain.validator.ValidationResult.VALID_REQUEST;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;

import java.util.List;

import org.springframework.stereotype.Service;

import com.microservicesteam.adele.clerk.domain.TicketRepository;
import com.microservicesteam.adele.ticketmaster.model.TicketId;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class TicketValidator {

    private final TicketRepository ticketRepository;

    public ValidationResult validate(List<TicketId> tickets) {
        if (tickets.isEmpty()) {
            log.warn("Empty tickets in request");
            return INVALID_NO_TICKET;
        }

        if (!allTicketsAreValid(tickets)) {
            log.warn("Tickets out of sector in request {}", tickets);
            return INVALID_TICKETS_OUT_OF_SECTOR;
        }

        if (!allTicketsAreFree(tickets)) {
            log.warn("Tickets already reserved/sold in request {}", tickets);
            return INVALID_TICKETS_RESERVED;
        }

        return VALID_REQUEST;
    }

    private boolean allTicketsAreValid(List<TicketId> ticketIds) {
        return ticketIds.stream()
                .allMatch(ticketRepository::has);
    }

    private boolean allTicketsAreFree(List<TicketId> ticketIds) {
        return ticketIds.stream()
                .allMatch(ticketId -> ticketRepository.get(ticketId).status() == FREE);
    }

}

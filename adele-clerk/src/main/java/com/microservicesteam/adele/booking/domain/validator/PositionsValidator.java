package com.microservicesteam.adele.booking.domain.validator;

import static com.microservicesteam.adele.booking.domain.validator.ValidationResult.INVALID_POSITIONS_BOOKED;
import static com.microservicesteam.adele.booking.domain.validator.ValidationResult.INVALID_POSITIONS_EMPTY;
import static com.microservicesteam.adele.booking.domain.validator.ValidationResult.INVALID_POSITIONS_OUT_OF_SECTOR;
import static com.microservicesteam.adele.booking.domain.validator.ValidationResult.VALID_REQUEST;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;

import java.util.List;

import org.springframework.stereotype.Service;

import com.microservicesteam.adele.booking.domain.TicketRepository;
import com.microservicesteam.adele.ticketmaster.model.Position;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class PositionsValidator {

    private final TicketRepository ticketRepository;

    public ValidationResult validate(List<Position> positions) {
        if (positions.isEmpty()) {
            log.warn("Empty positions in request");
            return INVALID_POSITIONS_EMPTY;
        }

        if (!allPositionsAreValid(positions)) {
            log.warn("Positions out of sector in request {}", positions);
            return INVALID_POSITIONS_OUT_OF_SECTOR;
        }

        if (!allPositionsAreFree(positions)) {
            log.warn("Positions already booked in request {}", positions);
            return INVALID_POSITIONS_BOOKED;
        }

        return VALID_REQUEST;
    }

    private boolean allPositionsAreValid(List<Position> positions) {
        return positions.stream()
                .allMatch(ticketRepository::has);
    }

    private boolean allPositionsAreFree(List<Position> positions) {
        return positions.stream()
                .allMatch(position -> ticketRepository.get(position).status() == FREE);
    }

}

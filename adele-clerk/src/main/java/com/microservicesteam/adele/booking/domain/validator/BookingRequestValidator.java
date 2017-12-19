package com.microservicesteam.adele.booking.domain.validator;

import static com.microservicesteam.adele.booking.domain.validator.ValidationResult.INVALID_POSITIONS_BOOKED;
import static com.microservicesteam.adele.booking.domain.validator.ValidationResult.INVALID_POSITIONS_EMPTY;
import static com.microservicesteam.adele.booking.domain.validator.ValidationResult.INVALID_POSITIONS_OUT_OF_SECTOR;
import static com.microservicesteam.adele.booking.domain.validator.ValidationResult.VALID_REQUEST;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.microservicesteam.adele.booking.domain.BookingRequest;
import com.microservicesteam.adele.booking.domain.TicketRepository;

@Service
@Slf4j
public class BookingRequestValidator {

    private final TicketRepository ticketRepository;

    public BookingRequestValidator(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public ValidationResult validate(BookingRequest request) {
        if (request.positions().isEmpty()) {
            log.warn("Empty positions in request {}", request);
            return INVALID_POSITIONS_EMPTY;
        }

        if (!allPositionsAreValid(request)) {
            log.warn("Positions out of sector in request {}", request);
            return INVALID_POSITIONS_OUT_OF_SECTOR;
        }

        if (!allPositionsAreFree(request)) {
            log.warn("Positions already booked in request {}", request);
            return INVALID_POSITIONS_BOOKED;
        }

        return VALID_REQUEST;
    }

    private boolean allPositionsAreValid(BookingRequest request) {
        return request.requestedPositions().stream()
                .allMatch(ticketRepository::has);
    }

    private boolean allPositionsAreFree(BookingRequest request) {
        return request.requestedPositions().stream()
                .allMatch(position -> ticketRepository.get(position).status() == FREE);
    }

}

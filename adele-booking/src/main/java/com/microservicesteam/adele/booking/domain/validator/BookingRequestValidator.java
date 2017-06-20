package com.microservicesteam.adele.booking.domain.validator;

import static com.microservicesteam.adele.booking.domain.validator.ValidationResult.*;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;

import org.springframework.stereotype.Service;

import com.microservicesteam.adele.booking.domain.BookingRequest;
import com.microservicesteam.adele.booking.domain.TicketRepository;

@Service
public class BookingRequestValidator {

    private final TicketRepository ticketRepository;

    public BookingRequestValidator(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public ValidationResult validate(BookingRequest request) {
        if (request.positions().isEmpty()) {
            return INVALID_POSITIONS_EMPTY;
        }

        if (!allPositionsAreValid(request)) {
            return INVALID_POSITIONS_OUT_OF_SECTOR;
        }

        if (!allPositionsAreFree(request)) {
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

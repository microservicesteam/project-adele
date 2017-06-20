package com.microservicesteam.adele.booking.domain.validator;

import static com.microservicesteam.adele.booking.domain.validator.ValidationResult.*;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.stereotype.Service;

import com.microservicesteam.adele.booking.domain.BookingRequest;
import com.microservicesteam.adele.booking.domain.TicketRepository;
import com.microservicesteam.adele.ticketmaster.model.Position;

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

        if (hasAlreadyBookedPosition(request)) {
            return INVALID_POSITIONS_BOOKED;
        }

        return VALID_REQUEST;
    }

    private boolean hasAlreadyBookedPosition(BookingRequest request) {
        List<Position> bookedPositions = request.requestedPositions().stream()
                .filter(position -> ticketRepository.get(position).status() != FREE)
                .collect(toList());

        return !bookedPositions.isEmpty();
    }

}

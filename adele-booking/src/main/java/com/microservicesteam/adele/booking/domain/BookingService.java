package com.microservicesteam.adele.booking.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.microservicesteam.adele.booking.boundary.web.EventPublisher;
import com.microservicesteam.adele.messaging.EventBasedService;
import com.microservicesteam.adele.ticketmaster.commands.BookTickets;
import com.microservicesteam.adele.ticketmaster.events.TicketsBooked;
import com.microservicesteam.adele.ticketmaster.model.Position;

@Service
public class BookingService extends EventBasedService {

    private final BookingIdGenerator bookingIdGenerator;
    private final EventPublisher eventPublisher;

    BookingService(EventBus eventBus, BookingIdGenerator bookingIdGenerator, EventPublisher eventPublisher) {
        super(eventBus);
        this.bookingIdGenerator = bookingIdGenerator;
        this.eventPublisher = eventPublisher;
    }

    public BookingResponse bookTickets(BookingRequest bookingRequest) {
        String bookingId = bookingIdGenerator.generateBookingId();

        List<Position> requestedPositions = bookingRequest.positions().stream()
                .map(positionId -> Position.builder()
                        .eventId(bookingRequest.eventId())
                        .sectorId(bookingRequest.sectorId())
                        .id(positionId)
                        .build())
                .collect(Collectors.toList());

        eventBus.post(BookTickets.builder()
                .bookingId(bookingId)
                .addAllPositions(requestedPositions)
                .build());

        return BookingResponse.builder()
                .withBookingId(bookingId)
                .build();
    }

    @Subscribe
    public void handleEvent(TicketsBooked ticketsBooked){
        eventPublisher.publish(ticketsBooked);
    }
}

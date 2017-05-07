package com.microservicesteam.adele.booking.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.microservicesteam.adele.booking.boundary.web.EventPublisher;
import com.microservicesteam.adele.messaging.EventBasedService;
import com.microservicesteam.adele.ticketmaster.commands.BookTickets;
import com.microservicesteam.adele.ticketmaster.events.TicketsBooked;
import com.microservicesteam.adele.ticketmaster.events.TicketsCancelled;
import com.microservicesteam.adele.ticketmaster.events.TicketsCreated;
import com.microservicesteam.adele.ticketmaster.model.BookedTicket;
import com.microservicesteam.adele.ticketmaster.model.FreeTicket;
import com.microservicesteam.adele.ticketmaster.model.Position;
import com.microservicesteam.adele.ticketmaster.model.Ticket;

@Service
public class BookingService extends EventBasedService {

    private final BookingIdGenerator bookingIdGenerator;
    private final EventPublisher eventPublisher;
    Map<Position, Ticket> ticketRepository;


    BookingService(EventBus eventBus, BookingIdGenerator bookingIdGenerator, EventPublisher eventPublisher) {
        super(eventBus);
        this.bookingIdGenerator = bookingIdGenerator;
        this.eventPublisher = eventPublisher;
        ticketRepository = new HashMap<>();
    }

    public BookingResponse bookTickets(BookingRequest bookingRequest) {
        String bookingId = bookingIdGenerator.generateBookingId();

        List<Position> requestedPositions = toPositions(bookingRequest);

        eventBus.post(BookTickets.builder()
                .bookingId(bookingId)
                .addAllPositions(requestedPositions)
                .build());

        return BookingResponse.builder()
                .withBookingId(bookingId)
                .build();
    }

    @Subscribe
    public void handleEvent(TicketsCreated ticketsCreated) {
        ticketsCreated.positions()
                .forEach(position -> ticketRepository.put(position, FreeTicket.builder()
                        .position(position)
                        .build()));
        eventPublisher.publish(ticketsCreated);
    }

    @Subscribe
    public void handleEvent(TicketsBooked ticketsBooked) {
        ticketsBooked.positions()
                .forEach(position -> ticketRepository.put(position, BookedTicket.builder()
                        .position(position)
                        .bookingId(ticketsBooked.bookingId())
                        .build()));
        eventPublisher.publish(ticketsBooked);
    }

    @Subscribe
    public void handleEvent(TicketsCancelled ticketsCancelledBooked) {
        ticketsCancelledBooked.positions()
                .forEach(position -> ticketRepository.put(position, FreeTicket.builder()
                        .position(position)
                        .build()));
        eventPublisher.publish(ticketsCancelledBooked);
    }

    private List<Position> toPositions(BookingRequest bookingRequest) {
        return bookingRequest.positions().stream()
                .map(positionId -> Position.builder()
                        .eventId(bookingRequest.eventId())
                        .sectorId(bookingRequest.sectorId())
                        .id(positionId)
                        .build())
                .collect(Collectors.toList());
    }
}

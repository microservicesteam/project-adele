package com.microservicesteam.adele.booking.domain;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.Map;

import com.microservicesteam.adele.ticketmaster.events.TicketsWereAlreadyBooked;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.microservicesteam.adele.booking.boundary.web.WebSocketEventPublisher;
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
    private final WebSocketEventPublisher webSocketEventPublisher;
    private final Map<Position, Ticket> ticketRepository;

    BookingService(EventBus eventBus,
                   BookingIdGenerator bookingIdGenerator,
                   WebSocketEventPublisher webSocketEventPublisher) {
        super(eventBus);
        this.bookingIdGenerator = bookingIdGenerator;
        this.webSocketEventPublisher = webSocketEventPublisher;
        ticketRepository = new HashMap<>();
    }

    public ImmutableList<Ticket> getTicketsStatus() {
        return ticketRepository.values().stream()
                .sorted(comparingInt(t -> t.position().id()))
                .collect(collectingAndThen(toList(), ImmutableList::copyOf));
    }

    public BookingResponse bookTickets(BookingRequest bookingRequest) {
        String bookingId = bookingIdGenerator.generateBookingId();

        ImmutableList<Position> requestedPositions = toPositions(bookingRequest);

        eventBus.post(BookTickets.builder()
                .bookingId(bookingId)
                .addAllPositions(requestedPositions)
                .build());

        return BookingResponse.builder()
                .bookingId(bookingId)
                .build();
    }

    @Subscribe
    public void handleEvent(TicketsCreated ticketsCreated) {
        ticketsCreated.positions()
                .forEach(position -> ticketRepository.put(position, FreeTicket.builder()
                        .position(position)
                        .build()));
    }

    @Subscribe
    public void handleEvent(TicketsBooked ticketsBooked) {
        ticketsBooked.positions()
                .forEach(position -> ticketRepository.put(position, BookedTicket.builder()
                        .position(position)
                        .bookingId(ticketsBooked.bookingId())
                        .build()));
        webSocketEventPublisher.publish(ticketsBooked);
    }

    @Subscribe
    public void handleEvent(TicketsCancelled ticketsCancelled) {
        ticketsCancelled.positions()
                .forEach(position -> ticketRepository.put(position, FreeTicket.builder()
                        .position(position)
                        .build()));
        webSocketEventPublisher.publish(ticketsCancelled);
    }

    @Subscribe
    public void handleEvent(TicketsWereAlreadyBooked ticketsWereAlreadyBooked) {
        webSocketEventPublisher.publish(ticketsWereAlreadyBooked);
    }

    private ImmutableList<Position> toPositions(BookingRequest bookingRequest) {
        return bookingRequest.positions().stream()
                .map(positionId -> Position.builder()
                        .eventId(bookingRequest.eventId())
                        .sectorId(bookingRequest.sectorId())
                        .id(positionId)
                        .build())
                .collect(collectingAndThen(toList(), ImmutableList::copyOf));
    }
}

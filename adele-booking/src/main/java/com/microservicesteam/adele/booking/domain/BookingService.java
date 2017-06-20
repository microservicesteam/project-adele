package com.microservicesteam.adele.booking.domain;

import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.microservicesteam.adele.booking.boundary.web.WebSocketEventPublisher;
import com.microservicesteam.adele.booking.domain.validator.BookingRequestValidator;
import com.microservicesteam.adele.booking.domain.validator.ValidationResult;
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

    private final BookingRequestValidator validator;
    private final BookingIdGenerator bookingIdGenerator;
    private final WebSocketEventPublisher webSocketEventPublisher;
    private final TicketRepository ticketRepository;

    BookingService(EventBus eventBus,
                   BookingRequestValidator validator,
                   BookingIdGenerator bookingIdGenerator,
                   WebSocketEventPublisher webSocketEventPublisher,
                   TicketRepository ticketRepository) {
        super(eventBus);
        this.validator = validator;
        this.bookingIdGenerator = bookingIdGenerator;
        this.webSocketEventPublisher = webSocketEventPublisher;
        this.ticketRepository = ticketRepository;
    }

    public ImmutableList<Ticket> getTicketsStatus() {
        return ticketRepository.getTicketsStatus();
    }

    public BookingResponse bookTickets(BookingRequest bookingRequest) {
        ValidationResult validationResult = validator.validate(bookingRequest);

        if (!validationResult.isValid()) {
            return BookingRejected.fromValidationResult(validationResult);
        }

        String bookingId = bookingIdGenerator.generateBookingId();

        ImmutableList<Position> requestedPositions = bookingRequest.requestedPositions();

        eventBus.post(BookTickets.builder()
                .bookingId(bookingId)
                .addAllPositions(requestedPositions)
                .build());

        return BookingRequested.builder()
                .bookingId(bookingId)
                .build();
    }

    @Subscribe
    public void handleEvent(TicketsCreated ticketsCreated) {
        ticketsCreated.positions()
                .forEach(position -> ticketRepository.put(FreeTicket.builder()
                        .position(position)
                        .build()));
    }

    @Subscribe
    public void handleEvent(TicketsBooked ticketsBooked) {
        ticketsBooked.positions()
                .forEach(position -> ticketRepository.put(BookedTicket.builder()
                        .position(position)
                        .bookingId(ticketsBooked.bookingId())
                        .build()));
        webSocketEventPublisher.publish(ticketsBooked);
    }

    @Subscribe
    public void handleEvent(TicketsCancelled ticketsCancelledBooked) {
        ticketsCancelledBooked.positions()
                .forEach(position -> ticketRepository.put(FreeTicket.builder()
                        .position(position)
                        .build()));
        webSocketEventPublisher.publish(ticketsCancelledBooked);
    }
}
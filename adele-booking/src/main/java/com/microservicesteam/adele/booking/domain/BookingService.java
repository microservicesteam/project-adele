package com.microservicesteam.adele.booking.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.microservicesteam.adele.booking.boundary.web.WebSocketEventPublisher;
import com.microservicesteam.adele.booking.domain.validator.BookingRequestValidator;
import com.microservicesteam.adele.booking.domain.validator.ValidationResult;
import com.microservicesteam.adele.messaging.EventBasedService;
import com.microservicesteam.adele.ticketmaster.commands.BookTickets;
import com.microservicesteam.adele.ticketmaster.events.TicketsAlreadyBooked;
import com.microservicesteam.adele.ticketmaster.events.TicketsBooked;
import com.microservicesteam.adele.ticketmaster.events.TicketsCancelled;
import com.microservicesteam.adele.ticketmaster.events.TicketsCreated;
import com.microservicesteam.adele.ticketmaster.model.BookedTicket;
import com.microservicesteam.adele.ticketmaster.model.FreeTicket;
import com.microservicesteam.adele.ticketmaster.model.Position;
import com.microservicesteam.adele.ticketmaster.model.Ticket;

@Service
public class BookingService extends EventBasedService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookingService.class);
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

    public ImmutableList<Ticket> getTicketsStatus(long eventId) {
        return ticketRepository.getTicketsStatusByEvent(eventId);
    }

    public BookingResponse bookTickets(BookingRequest bookingRequest) {
        ValidationResult validationResult = validator.validate(bookingRequest);

        if (!validationResult.isValid()) {
            return BookingRejected.fromValidationResult(validationResult);
        }

        String bookingId = bookingIdGenerator.generateBookingId();
        LOGGER.debug("BookingId {} gereate4d to request {}", bookingId, bookingRequest);

        ImmutableList<Position> requestedPositions = bookingRequest.requestedPositions();

        BookTickets bookTickets = BookTickets.builder()
                .bookingId(bookingId)
                .addAllPositions(requestedPositions)
                .build();
        LOGGER.debug("Command was posted {}", bookTickets);
        eventBus.post(bookTickets);

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
        LOGGER.debug("Event was processed {}", ticketsCreated);
    }

    @Subscribe
    public void handleEvent(TicketsBooked ticketsBooked) {
        ticketsBooked.positions()
                .forEach(position -> ticketRepository.put(BookedTicket.builder()
                        .position(position)
                        .bookingId(ticketsBooked.bookingId())
                        .build()));
        LOGGER.debug("Event published on websocket {}", ticketsBooked);
        webSocketEventPublisher.publish(ticketsBooked);
    }

    @Subscribe
    public void handleEvent(TicketsCancelled ticketsCancelled) {
        ticketsCancelled.positions()
                .forEach(position -> ticketRepository.put(FreeTicket.builder()
                        .position(position)
                        .build()));
        LOGGER.debug("Event published on websocket {}", ticketsCancelled);
        webSocketEventPublisher.publish(ticketsCancelled);
    }

    @Subscribe
    public void handleEvent(TicketsAlreadyBooked ticketsAlreadyBooked) {
        LOGGER.debug("Event published on websocket {}", ticketsAlreadyBooked);
        webSocketEventPublisher.publish(ticketsAlreadyBooked);
    }
}

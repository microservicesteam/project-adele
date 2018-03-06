package com.microservicesteam.adele.clerk.domain;

import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.RESERVED;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.microservicesteam.adele.clerk.boundary.web.WebSocketEventPublisher;
import com.microservicesteam.adele.clerk.domain.validator.TicketValidator;
import com.microservicesteam.adele.clerk.domain.validator.ValidationResult;
import com.microservicesteam.adele.messaging.EventBasedService;
import com.microservicesteam.adele.ticketmaster.commands.CreateReservation;
import com.microservicesteam.adele.ticketmaster.events.ReservationAccepted;
import com.microservicesteam.adele.ticketmaster.events.ReservationCancelled;
import com.microservicesteam.adele.ticketmaster.events.ReservationRejected;
import com.microservicesteam.adele.ticketmaster.model.Reservation;
import com.microservicesteam.adele.ticketmaster.model.Ticket;
import com.microservicesteam.adele.ticketmaster.model.TicketId;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReservationsService extends EventBasedService {

    private final TicketRepository ticketRepository;
    private final WebSocketEventPublisher webSocketEventPublisher;
    private final TicketValidator ticketValidator;
    private final ReservationIdGenerator reservationIdGenerator;

    public ReservationsService(EventBus eventBus,
            TicketRepository ticketRepository,
            WebSocketEventPublisher webSocketEventPublisher,
            TicketValidator ticketValidator,
            ReservationIdGenerator reservationIdGenerator) {
        super(eventBus);
        this.ticketRepository = ticketRepository;
        this.webSocketEventPublisher = webSocketEventPublisher;
        this.ticketValidator = ticketValidator;
        this.reservationIdGenerator = reservationIdGenerator;
    }

    public ReservationResponse reserveTickets(List<TicketId> ticketIds) {
        ValidationResult validationResult = ticketValidator.validate(ticketIds);

        if (!validationResult.isValid()) {
            return com.microservicesteam.adele.clerk.domain.ReservationRejected.fromValidationResult(validationResult);
        }

        String reservationId = reservationIdGenerator.generateReservationId();

        CreateReservation createReservationCommand = CreateReservation.builder()
                .reservation(Reservation.builder()
                        .reservationId(reservationId)
                        .addAllTickets(ticketIds)
                        .build())
                .build();

        log.debug("Reservation initiated: {}", createReservationCommand);
        eventBus.post(createReservationCommand);

        return ReservationRequested.builder()
                .reservationId(reservationId)
                .build();
    }

    @Subscribe
    public void handleEvent(ReservationAccepted reservationAccepted) {
        reservationAccepted.reservation().tickets()
                .forEach(ticketId -> ticketRepository.put(Ticket.builder()
                        .status(RESERVED)
                        .ticketId(ticketId)
                        .build()));
        webSocketEventPublisher.publishToSector(reservationAccepted);
    }

    @Subscribe
    public void handleEvent(ReservationCancelled reservationCancelled) {
        reservationCancelled.reservation().tickets()
                .forEach(ticketId -> ticketRepository.put(Ticket.builder()
                        .status(FREE)
                        .ticketId(ticketId)
                        .build()));
        webSocketEventPublisher.publishToSector(reservationCancelled);
    }

    @Subscribe
    public void handleEvent(ReservationRejected reservationRejected) {
        webSocketEventPublisher.publishToSector(reservationRejected);
    }
}

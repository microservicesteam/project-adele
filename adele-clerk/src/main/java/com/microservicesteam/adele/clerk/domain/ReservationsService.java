package com.microservicesteam.adele.clerk.domain;

import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.RESERVED;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.microservicesteam.adele.clerk.boundary.web.WebSocketEventPublisher;
import com.microservicesteam.adele.clerk.domain.validator.PositionsValidator;
import com.microservicesteam.adele.clerk.domain.validator.ValidationResult;
import com.microservicesteam.adele.messaging.EventBasedService;
import com.microservicesteam.adele.ticketmaster.commands.CreateReservation;
import com.microservicesteam.adele.ticketmaster.events.ReservationAccepted;
import com.microservicesteam.adele.ticketmaster.events.ReservationCancelled;
import com.microservicesteam.adele.ticketmaster.events.ReservationRejected;
import com.microservicesteam.adele.ticketmaster.model.Position;
import com.microservicesteam.adele.ticketmaster.model.Reservation;
import com.microservicesteam.adele.ticketmaster.model.Ticket;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReservationsService extends EventBasedService {

    private final TicketRepository ticketRepository;
    private final WebSocketEventPublisher webSocketEventPublisher;
    private final PositionsValidator validator;
    private final ReservationIdGenerator reservationIdGenerator;

    public ReservationsService(EventBus eventBus,
            TicketRepository ticketRepository,
            WebSocketEventPublisher webSocketEventPublisher,
            PositionsValidator validator,
            ReservationIdGenerator reservationIdGenerator) {
        super(eventBus);
        this.ticketRepository = ticketRepository;
        this.webSocketEventPublisher = webSocketEventPublisher;
        this.validator = validator;
        this.reservationIdGenerator = reservationIdGenerator;
    }

    public ReservationResponse reservePositions(List<Position> positions) {
        ValidationResult validationResult = validator.validate(positions);

        if (!validationResult.isValid()) {
            return com.microservicesteam.adele.clerk.domain.ReservationRejected.fromValidationResult(validationResult);
        }

        String reservationId = reservationIdGenerator.generateReservationId();
        log.debug("Reservation id {} generated for positions {}", reservationId, positions);

        CreateReservation createReservationCommand = CreateReservation.builder()
                .reservation(Reservation.builder()
                        .reservationId(reservationId)
                        .addAllPositions(positions)
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
        reservationAccepted.reservation().positions()
                .forEach(position -> ticketRepository.put(Ticket.builder()
                        .status(RESERVED)
                        .position(position)
                        .build()));
        webSocketEventPublisher.publishToSector(reservationAccepted);
    }

    @Subscribe
    public void handleEvent(ReservationCancelled reservationCancelled) {
        reservationCancelled.reservation().positions()
                .forEach(position -> ticketRepository.put(Ticket.builder()
                        .status(FREE)
                        .position(position)
                        .build()));
        webSocketEventPublisher.publishToSector(reservationCancelled);
    }

    @Subscribe
    public void handleEvent(ReservationRejected reservationRejected) {
        webSocketEventPublisher.publishToSector(reservationRejected);
    }
}

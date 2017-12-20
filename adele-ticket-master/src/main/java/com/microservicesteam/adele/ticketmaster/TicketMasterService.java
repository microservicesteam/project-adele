package com.microservicesteam.adele.ticketmaster;

import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.BOOKED;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.microservicesteam.adele.messaging.EventBasedService;
import com.microservicesteam.adele.ticketmaster.commands.CancelReservation;
import com.microservicesteam.adele.ticketmaster.commands.CreateReservation;
import com.microservicesteam.adele.ticketmaster.commands.CreateTickets;
import com.microservicesteam.adele.ticketmaster.events.ReservationAccepted;
import com.microservicesteam.adele.ticketmaster.events.ReservationCancelled;
import com.microservicesteam.adele.ticketmaster.events.ReservationRejected;
import com.microservicesteam.adele.ticketmaster.events.TicketsCreated;
import com.microservicesteam.adele.ticketmaster.exceptions.NoOperation;
import com.microservicesteam.adele.ticketmaster.model.Position;
import com.microservicesteam.adele.ticketmaster.model.Reservation;
import com.microservicesteam.adele.ticketmaster.model.Ticket;
import com.microservicesteam.adele.ticketmaster.model.TicketStatus;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TicketMasterService extends EventBasedService {

    Map<Position, TicketStatus> ticketRepository;

    TicketMasterService(EventBus eventBus) {
        super(eventBus);
        ticketRepository = new HashMap<>();
    }

    @Subscribe
    public void handleCommand(CreateTickets command) {
        if (positionsNotExist(command.tickets())) {
            addTickets(command);
            TicketsCreated ticketsCreated = TicketsCreated.builder()
                    .addAllTickets(command.tickets())
                    .build();
            log.info("{} tickets created", ticketsCreated.tickets().size());
            eventBus.post(ticketsCreated);
        } else {
            eventBus.post(NoOperation.builder()
                    .sourceCommand(command)
                    .build());
        }
    }

    @Subscribe
    public void handleCommand(CreateReservation command) {
        Reservation reservation = command.reservation();
        if (positionsFree(reservation.positions())) {
            reservePositions(reservation.positions());
            ReservationAccepted reservationAccepted = ReservationAccepted.builder()
                    .reservation(Reservation.builder()
                            .reservationId(reservation.reservationId())
                            .addAllPositions(reservation.positions())
                            .build())
                    .build();
            log.info("Tickets reserved: {}", reservationAccepted);
            eventBus.post(reservationAccepted);
        } else {
            ReservationRejected reservationRejected = ReservationRejected.builder()
                    .reservation(Reservation.builder()
                            .reservationId(reservation.reservationId())
                            .addAllPositions(reservation.positions())
                            .build())
                    .build();
            log.debug("Tickets reservation rejected: {}", reservationRejected);
            eventBus.post(reservationRejected);
        }
    }

    @Subscribe
    public void handleCommand(CancelReservation command) {
        Reservation reservation = command.reservation();
        if (positionsBooked(reservation.positions())) {
            freePositions(reservation.positions());
            ReservationCancelled reservationCancelled = ReservationCancelled.builder()
                    .reservation(Reservation.builder()
                            .reservationId(reservation.reservationId())
                            .addAllPositions(reservation.positions())
                            .build())
                    .build();
            log.info("Reservations cancelled: {}", reservationCancelled);
            eventBus.post(reservationCancelled);
        } else {
            eventBus.post(NoOperation.builder()
                    .sourceCommand(command)
                    .build());
        }
    }

    private boolean positionsNotExist(List<Ticket> tickets) {
        return tickets.stream()
                .map(Ticket::position)
                .noneMatch(position -> ticketRepository.containsKey(position));
    }

    private boolean positionsFree(List<Position> positions) {
        return positionsInState(positions, FREE);
    }

    private boolean positionsBooked(List<Position> positions) {
        return positionsInState(positions, BOOKED);
    }

    private boolean positionsInState(List<Position> positions, TicketStatus status) {
        return positions.stream().allMatch(
                position -> status == ticketRepository.get(position));
    }

    private void addTickets(CreateTickets command) {
        command.tickets().forEach(ticket -> ticketRepository.put(ticket.position(), ticket.status()));
    }

    private void reservePositions(List<Position> positions) {
        positions.forEach(
                position -> ticketRepository.put(position, BOOKED));
    }

    private void freePositions(List<Position> positions) {
        positions.forEach(
                position -> {
                    if (ticketRepository.containsKey(position)) {
                        ticketRepository.replace(position, FREE);
                    }
                });
    }
}

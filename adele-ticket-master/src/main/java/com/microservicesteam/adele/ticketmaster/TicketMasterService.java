package com.microservicesteam.adele.ticketmaster;

import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.RESERVED;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.SOLD;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.microservicesteam.adele.messaging.EventBasedService;
import com.microservicesteam.adele.ticketmaster.commands.CancelReservation;
import com.microservicesteam.adele.ticketmaster.commands.CloseReservation;
import com.microservicesteam.adele.ticketmaster.commands.CreateReservation;
import com.microservicesteam.adele.ticketmaster.commands.CreateTickets;
import com.microservicesteam.adele.ticketmaster.events.ReservationAccepted;
import com.microservicesteam.adele.ticketmaster.events.ReservationCancelled;
import com.microservicesteam.adele.ticketmaster.events.ReservationClosed;
import com.microservicesteam.adele.ticketmaster.events.ReservationRejected;
import com.microservicesteam.adele.ticketmaster.events.TicketsCreated;
import com.microservicesteam.adele.ticketmaster.exceptions.NoOperation;
import com.microservicesteam.adele.ticketmaster.model.Reservation;
import com.microservicesteam.adele.ticketmaster.model.Ticket;
import com.microservicesteam.adele.ticketmaster.model.TicketId;
import com.microservicesteam.adele.ticketmaster.model.TicketStatus;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TicketMasterService extends EventBasedService {

    Map<TicketId, TicketStatus> ticketRepository;

    TicketMasterService(EventBus eventBus) {
        super(eventBus);
        ticketRepository = new HashMap<>();
    }

    @Subscribe
    public void handleCommand(CreateTickets command) {
        if (ticketsNotExist(command.tickets())) {
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
        if (ticketsFree(reservation.tickets())) {
            reserveTickets(reservation.tickets());
            ReservationAccepted reservationAccepted = ReservationAccepted.builder()
                    .reservation(reservation)
                    .build();
            log.info("Tickets reserved: {}", reservationAccepted);
            eventBus.post(reservationAccepted);
        } else {
            ReservationRejected reservationRejected = ReservationRejected.builder()
                    .reservation(reservation)
                    .build();
            log.debug("Tickets reservation rejected: {}", reservationRejected);
            eventBus.post(reservationRejected);
        }
    }

    @Subscribe
    public void handleCommand(CancelReservation command) {
        Reservation reservation = command.reservation();
        if (ticketsReserved(reservation.tickets())) {
            freeTickets(reservation.tickets());
            ReservationCancelled reservationCancelled = ReservationCancelled.builder()
                    .reservation(reservation)
                    .build();
            log.info("Reservations cancelled: {}", reservationCancelled);
            eventBus.post(reservationCancelled);
        } else {
            eventBus.post(NoOperation.builder()
                    .sourceCommand(command)
                    .build());
        }
    }

    @Subscribe
    public void handleCommand(CloseReservation command) {
        Reservation reservation = command.reservation();
        if (ticketsReserved(reservation.tickets())) {
            sellTickets(reservation.tickets());
            ReservationClosed reservationClosed = ReservationClosed.builder()
                    .reservation(reservation)
                    .build();
            log.info("Reservation closed: {}", reservationClosed);
            eventBus.post(reservationClosed);
        } else {
            eventBus.post(NoOperation.builder()
                    .sourceCommand(command)
                    .build());
        }
    }

    private boolean ticketsNotExist(List<Ticket> tickets) {
        return tickets.stream()
                .map(Ticket::ticketId)
                .noneMatch(ticketId -> ticketRepository.containsKey(ticketId));
    }

    private boolean ticketsFree(List<TicketId> ticketIds) {
        return ticketsInState(ticketIds, FREE);
    }

    private boolean ticketsReserved(List<TicketId> ticketIds) {
        return ticketsInState(ticketIds, RESERVED);
    }

    private boolean ticketsInState(List<TicketId> ticketIds, TicketStatus status) {
        return ticketIds.stream().allMatch(
                ticketId -> status == ticketRepository.get(ticketId));
    }

    private void addTickets(CreateTickets command) {
        command.tickets().forEach(ticket -> ticketRepository.put(ticket.ticketId(), ticket.status()));
    }

    private void reserveTickets(List<TicketId> ticketIds) {
        ticketIds.forEach(setTicketStateTo(RESERVED));
    }

    private void freeTickets(List<TicketId> ticketIds) {
        ticketIds.forEach(setTicketStateTo(FREE));
    }

    private void sellTickets(List<TicketId> ticketIds) {
        ticketIds.forEach(setTicketStateTo(SOLD));
    }

    private Consumer<TicketId> setTicketStateTo(TicketStatus status) {
        return ticketId -> ticketRepository.replace(ticketId, status);
    }
}

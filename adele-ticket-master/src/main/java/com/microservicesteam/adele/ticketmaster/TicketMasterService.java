package com.microservicesteam.adele.ticketmaster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.microservicesteam.adele.ticketmaster.events.TicketsAlreadyBooked;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.microservicesteam.adele.messaging.EventBasedService;
import com.microservicesteam.adele.ticketmaster.commands.CreateTickets;
import com.microservicesteam.adele.ticketmaster.events.TicketsBooked;
import com.microservicesteam.adele.ticketmaster.events.TicketsCancelled;
import com.microservicesteam.adele.ticketmaster.events.TicketsCreated;
import com.microservicesteam.adele.ticketmaster.exceptions.NoOperation;
import com.microservicesteam.adele.ticketmaster.model.Ticket;

@Service
@Slf4j
public class TicketMasterService extends EventBasedService {

    Map<Position, Ticket> ticketRepository;

    TicketMasterService(EventBus eventBus) {
        super(eventBus);
        ticketRepository = new HashMap<>();
    }

    @Subscribe
    public void handleCommand(CreateTickets command) {
        if (positionsNotExist(command.positions())) {
            addTickets(command);
            TicketsCreated ticketsCreated = TicketsCreated.builder()
                    .addAllPositions(command.positions())
                    .build();
            log.info("{} tickets created", ticketsCreated.positions().size());
            eventBus.post(ticketsCreated);
        } else {
            eventBus.post(NoOperation.builder()
                    .sourceCommand(command)
                    .build());
        }
    }


    @Subscribe
    public void handleCommand(BookTickets command) {
        if (positionsFree(command.positions())) {
            bookTickets(command);
            TicketsBooked ticketsBooked = TicketsBooked.builder()
                    .bookingId(command.bookingId())
                    .addAllPositions(command.positions())
                    .build();
            log.info("Tickets booked: {}", ticketsBooked);
            eventBus.post(ticketsBooked);
        } else {
            TicketsAlreadyBooked ticketsAlreadyBooked = TicketsAlreadyBooked.builder()
                    .bookingId(command.bookingId())
                    .addAllPositions(command.positions())
                    .build();
            log.debug("Unsuccessful booking attempt: {}", ticketsAlreadyBooked);
            eventBus.post(ticketsAlreadyBooked);
        }
    }

    @Subscribe
    public void handleCommand(CancelTickets command) {
        if (positionsBooked(command.positions())) {
            cancelTickets(command);
            TicketsCancelled ticketsCancelled = TicketsCancelled.builder()
                    .bookingId(command.bookingId())
                    .addAllPositions(command.positions())
                    .build();
            log.info("Tickets cancelled: {}", ticketsCancelled);
            eventBus.post(ticketsCancelled);
        } else {
            eventBus.post(NoOperation.builder()
                    .sourceCommand(command)
                    .build());
        }
    }

    private boolean positionsNotExist(List<Position> positions) {
        return positions.stream()
                .noneMatch(position -> ticketRepository.containsKey(position));
    }

    private boolean positionsFree(List<Position> positions) {
        return positions.stream().allMatch(
                position -> ticketRepository.containsKey(position) &&
                        ticketRepository.get(position) instanceof FreeTicket);
    }

    private boolean positionsBooked(List<Position> positions) {
        return positions.stream().allMatch(
                position -> ticketRepository.containsKey(position) &&
                        ticketRepository.get(position) instanceof BookedTicket);
    }

    private void addTickets(CreateTickets command) {
        command.positions().forEach(
                position -> ticketRepository.put(position,
                        FreeTicket.builder()
                                .position(position)
                                .build()));
    }

    private void bookTickets(BookTickets command) {
        command.positions().forEach(
                position -> ticketRepository.put(position,
                        BookedTicket.builder()
                                .bookingId(command.bookingId())
                                .position(position)
                                .build()));
    }

    private void cancelTickets(CancelTickets command) {
        command.positions().forEach(
                position -> {
                    if (ticketRepository.containsKey(position)) {
                        ticketRepository.replace(position,
                                FreeTicket.builder()
                                        .position(position)
                                        .build());
                    }
                });
    }
}

package com.microservicesteam.adele.ticketmaster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.microservicesteam.adele.ticketmaster.events.TicketsWereAlreadyBooked;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.microservicesteam.adele.messaging.EventBasedService;
import com.microservicesteam.adele.ticketmaster.commands.BookTickets;
import com.microservicesteam.adele.ticketmaster.commands.CancelTickets;
import com.microservicesteam.adele.ticketmaster.commands.CreateTickets;
import com.microservicesteam.adele.ticketmaster.events.TicketsBooked;
import com.microservicesteam.adele.ticketmaster.events.TicketsCancelled;
import com.microservicesteam.adele.ticketmaster.events.TicketsCreated;
import com.microservicesteam.adele.ticketmaster.exceptions.NoOperation;
import com.microservicesteam.adele.ticketmaster.model.BookedTicket;
import com.microservicesteam.adele.ticketmaster.model.FreeTicket;
import com.microservicesteam.adele.ticketmaster.model.Position;
import com.microservicesteam.adele.ticketmaster.model.Ticket;

@Service
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
            eventBus.post(TicketsCreated.builder()
                    .addAllPositions(command.positions())
                    .build());
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
            eventBus.post(TicketsBooked.builder()
                    .bookingId(command.bookingId())
                    .addAllPositions(command.positions())
                    .build());
        } else {
            eventBus.post(TicketsWereAlreadyBooked.builder()
                    .sourceCommand(command)
                    .build());
        }
    }

    @Subscribe
    public void handleCommand(CancelTickets command) {
        if (positionsBooked(command.positions())) {
            cancelTickets(command);
            eventBus.post(TicketsCancelled.builder()
                    .bookingId(command.bookingId())
                    .addAllPositions(command.positions())
                    .build());
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

package com.microservicesteam.adele.ticketmaster;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.microservicesteam.adele.messaging.EventBasedService;
import com.microservicesteam.adele.ticketmaster.commands.BookTickets;
import com.microservicesteam.adele.ticketmaster.commands.CreateTickets;
import com.microservicesteam.adele.ticketmaster.events.TicketsBooked;
import com.microservicesteam.adele.ticketmaster.events.TicketsCreated;
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
        addTickets().accept(command);
        eventBus.post(TicketsCreated.builder()
                .addAllPositions(command.positions())
                .build());
    }

    @Subscribe
    public void handleCommand(BookTickets command) {
        bookTickets().accept(command);
        eventBus.post(TicketsBooked.builder()
                .bookingId(command.bookingId())
                .addAllPositions(command.positions())
                .build());
    }

    private Consumer<CreateTickets> addTickets() {
        return command -> command.positions().forEach(
                position -> ticketRepository.put(position,
                        FreeTicket.builder()
                                .position(position)
                                .build()));
    }

    private Consumer<BookTickets> bookTickets() {
        return command -> command.positions().forEach(
                position -> ticketRepository.put(position,
                        BookedTicket.builder()
                                .bookingId(command.bookingId())
                                .position(position)
                                .build()));
    }

}

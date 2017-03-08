package com.microservicesteam.adele.ticketmaster;

import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.BOOKED;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.microservicesteam.adele.messaging.EventBasedService;
import com.microservicesteam.adele.ticketmaster.events.TicketsBookedEvent;
import com.microservicesteam.adele.ticketmaster.events.TicketsClaimedEvent;
import com.microservicesteam.adele.ticketmaster.events.TicketsCreatedEvent;
import com.microservicesteam.adele.ticketmaster.model.TicketId;
import com.microservicesteam.adele.ticketmaster.model.TicketStatus;

@Service
public class TicketMasterService extends EventBasedService {

    Map<TicketId, TicketStatus> ticketRepository;

    TicketMasterService(EventBus eventBus) {
        super(eventBus);
        ticketRepository = new HashMap<>();
    }

    @Subscribe
    public void handleEvent(TicketsCreatedEvent event) {
        addTickets().accept(event);
    }

    @Subscribe
    public void handleEvent(TicketsClaimedEvent event) {
        bookTickets().accept(event);
        eventBus.post(TicketsBookedEvent.builder()
                .sectorId(event.sectorId())
                .addAllPositions(event.positions())
                .build());
    }

    Consumer<TicketsCreatedEvent> addTickets() {
        return ticketsAvailableEvent -> ticketsAvailableEvent.positions().forEach(
                position -> ticketRepository.put(TicketId.builder()
                        .sector(ticketsAvailableEvent.sectorId())
                        .position(position)
                        .build(), FREE));
    }

    Consumer<TicketsClaimedEvent> bookTickets() {
        return ticketsClaimedEvent -> ticketsClaimedEvent.positions().forEach(
                position -> {
                    ticketRepository.put(TicketId.builder()
                            .sector(ticketsClaimedEvent.sectorId())
                            .position(position)
                            .build(), BOOKED);
                });
    }

}

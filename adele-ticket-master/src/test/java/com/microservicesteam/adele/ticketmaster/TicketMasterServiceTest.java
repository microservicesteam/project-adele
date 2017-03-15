package com.microservicesteam.adele.ticketmaster;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import org.junit.Before;
import org.junit.Test;

import com.google.common.eventbus.EventBus;
import com.microservicesteam.adele.messaging.listeners.DeadEventListener;
import com.microservicesteam.adele.ticketmaster.commands.BookTickets;
import com.microservicesteam.adele.ticketmaster.commands.CreateTickets;
import com.microservicesteam.adele.ticketmaster.events.TicketsBookedEvent;
import com.microservicesteam.adele.ticketmaster.events.TicketsCreatedEvent;
import com.microservicesteam.adele.ticketmaster.model.BookedTicket;
import com.microservicesteam.adele.ticketmaster.model.FreeTicket;
import com.microservicesteam.adele.ticketmaster.model.Position;

public class TicketMasterServiceTest {

    private static Position POSITION_1 = Position.builder().sectorId(1).id(1).build();
    private static Position POSITION_2 = Position.builder().sectorId(1).id(2).build();

    private TicketMasterService ticketMasterService;
    private EventBus eventBus;
    private DeadEventListener deadEventListener;

    @Before
    public void setUp() throws Exception {
        eventBus = new EventBus();
        ticketMasterService = new TicketMasterService(eventBus);
        ticketMasterService.init();
        deadEventListener = new DeadEventListener(eventBus);
        deadEventListener.init();
    }

    @Test
    public void testAddTickets() throws Exception {

        createTickets(POSITION_1, POSITION_2);

        assertThat(ticketMasterService.ticketRepository)
                .hasSize(2)
                .contains(entry(POSITION_1, FreeTicket.builder()
                                .position(POSITION_1)
                                .build()),
                        entry(POSITION_2, FreeTicket.builder()
                                .position(POSITION_2)
                                .build()));
        assertThat(deadEventListener.deadEvents)
                .extracting("event")
                .containsExactly(TicketsCreatedEvent.builder()
                        .addPositions(POSITION_1, POSITION_2)
                        .build());
    }

    @Test
    public void testBookTickets() throws Exception {
        createTickets(POSITION_1, POSITION_2);
        bookTickets(POSITION_1);

        assertThat(ticketMasterService.ticketRepository)
                .hasSize(2)
                .contains(entry(POSITION_1, BookedTicket.builder()
                                .bookingId(1L)
                                .position(POSITION_1)
                                .build()),
                        entry(POSITION_2, FreeTicket.builder()
                                .position(POSITION_2)
                                .build()));
        assertThat(deadEventListener.deadEvents)
                .extracting("event")
                .containsExactly(
                        TicketsCreatedEvent.builder()
                                .addPositions(POSITION_1, POSITION_2)
                                .build(),
                        TicketsBookedEvent.builder()
                                .bookingId(1L)
                                .addPositions(POSITION_1)
                                .build());
    }

    private void bookTickets(Position... positions) {
        eventBus.post(BookTickets.builder()
                .bookingId(1L)
                .addPositions(positions)
                .build());
    }

    private void createTickets(Position... positions) {
        eventBus.post(CreateTickets.builder()
                .addPositions(positions)
                .build());
    }
}
package com.microservicesteam.adele.ticketmaster;

import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.BOOKED;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import org.junit.Before;
import org.junit.Test;

import com.google.common.eventbus.EventBus;
import com.microservicesteam.adele.messaging.listeners.DeadEventListener;
import com.microservicesteam.adele.ticketmaster.events.TicketsBookedEvent;
import com.microservicesteam.adele.ticketmaster.events.TicketsClaimedEvent;
import com.microservicesteam.adele.ticketmaster.events.TicketsCreatedEvent;
import com.microservicesteam.adele.ticketmaster.model.TicketId;

public class TicketMasterServiceTest {

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

        ticketsCreated();

        assertThat(ticketMasterService.ticketRepository)
                .hasSize(2)
                .contains(entry(ticketId(1, 1), FREE),
                        entry(ticketId(1, 3), FREE));
    }

    @Test
    public void testBookTickets() throws Exception {
        ticketsCreated();
        ticketBooked();

        assertThat(ticketMasterService.ticketRepository)
                .hasSize(2)
                .contains(entry(ticketId(1, 1), BOOKED),
                        entry(ticketId(1, 3), FREE));
        assertThat(deadEventListener.deadEvents)
                .extracting("event")
                .containsExactly(TicketsBookedEvent.builder()
                        .sectorId(1)
                        .addPositions(1)
                        .build());
    }

    private void ticketBooked() {
        eventBus.post(TicketsClaimedEvent.builder()
                .sectorId(1)
                .addPositions(1)
                .build());
    }

    private void ticketsCreated() {
        eventBus.post(TicketsCreatedEvent.builder()
                .sectorId(1)
                .addPositions(1, 3)
                .build());
    }

    private TicketId ticketId(int sectorId, int position) {
        return TicketId.builder()
                .sector(sectorId)
                .position(position)
                .build();
    }

}
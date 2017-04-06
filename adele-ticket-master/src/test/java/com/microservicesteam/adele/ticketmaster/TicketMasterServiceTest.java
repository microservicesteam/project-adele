package com.microservicesteam.adele.ticketmaster;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import org.junit.Before;
import org.junit.Test;

import com.google.common.eventbus.EventBus;
import com.microservicesteam.adele.messaging.listeners.DeadEventListener;
import com.microservicesteam.adele.ticketmaster.commands.BookTickets;
import com.microservicesteam.adele.ticketmaster.commands.CancelTickets;
import com.microservicesteam.adele.ticketmaster.commands.CreateTickets;
import com.microservicesteam.adele.ticketmaster.events.TicketsBooked;
import com.microservicesteam.adele.ticketmaster.events.TicketsCancelled;
import com.microservicesteam.adele.ticketmaster.events.TicketsCreated;
import com.microservicesteam.adele.ticketmaster.model.BookedTicket;
import com.microservicesteam.adele.ticketmaster.model.FreeTicket;
import com.microservicesteam.adele.ticketmaster.model.Position;

public class TicketMasterServiceTest {

    private static final long BOOKING_ID = 1L;
    private static final Position POSITION_1 = Position.builder().sectorId(1).id(1).eventId(1).build();
    private static final Position POSITION_2 = Position.builder().sectorId(1).id(2).eventId(1).build();

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
    public void createTicketsCommandResultsInTicketsCreatedEvent() throws Exception {

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
                .containsExactly(TicketsCreated.builder()
                        .addPositions(POSITION_1, POSITION_2)
                        .build());
    }

    @Test
    public void bookTicketsCommandResultsInTicketsBookEvent() throws Exception {
        createTickets(POSITION_1, POSITION_2);
        bookTickets(POSITION_1);

        assertThat(ticketMasterService.ticketRepository)
                .hasSize(2)
                .contains(entry(POSITION_1, BookedTicket.builder()
                                .bookingId(BOOKING_ID)
                                .position(POSITION_1)
                                .build()),
                        entry(POSITION_2, FreeTicket.builder()
                                .position(POSITION_2)
                                .build()));
        assertThat(deadEventListener.deadEvents)
                .extracting("event")
                .containsExactly(
                        TicketsCreated.builder()
                                .addPositions(POSITION_1, POSITION_2)
                                .build(),
                        TicketsBooked.builder()
                                .bookingId(BOOKING_ID)
                                .addPositions(POSITION_1)
                                .build());
    }

    @Test
    public void cancelTicketsCommandResultsInTicketsCancelledEvent() throws Exception {
        createTickets(POSITION_1, POSITION_2);
        bookTickets(POSITION_1);
        cancelTickets(POSITION_1);

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
                .containsExactly(
                        TicketsCreated.builder()
                                .addPositions(POSITION_1, POSITION_2)
                                .build(),
                        TicketsBooked.builder()
                                .bookingId(BOOKING_ID)
                                .addPositions(POSITION_1)
                                .build(),
                        TicketsCancelled.builder()
                                .bookingId(BOOKING_ID)
                                .addPositions(POSITION_1)
                                .build());
    }

    private void bookTickets(Position... positions) {
        eventBus.post(BookTickets.builder()
                .bookingId(BOOKING_ID)
                .addPositions(positions)
                .build());
    }

    private void createTickets(Position... positions) {
        eventBus.post(CreateTickets.builder()
                .addPositions(positions)
                .build());
    }

    private void cancelTickets(Position... positions) {
        eventBus.post(CancelTickets.builder()
                .bookingId(BOOKING_ID)
                .addPositions(positions)
                .build());
    }
}
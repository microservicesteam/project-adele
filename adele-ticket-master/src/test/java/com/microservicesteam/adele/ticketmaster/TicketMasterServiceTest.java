package com.microservicesteam.adele.ticketmaster;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import com.microservicesteam.adele.ticketmaster.events.TicketsAlreadyBooked;
import org.junit.Before;
import org.junit.Test;

import com.google.common.eventbus.EventBus;
import com.microservicesteam.adele.messaging.listeners.DeadEventListener;
import com.microservicesteam.adele.ticketmaster.commands.CreateTickets;
import com.microservicesteam.adele.ticketmaster.events.TicketsBooked;
import com.microservicesteam.adele.ticketmaster.events.TicketsCancelled;
import com.microservicesteam.adele.ticketmaster.events.TicketsCreated;
import com.microservicesteam.adele.ticketmaster.exceptions.NoOperation;

public class TicketMasterServiceTest {

    private static final String BOOKING_ID = "abc-123";
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
    public void createTicketsCommandResultsInNoOperationException() throws Exception {

        createTickets(POSITION_1, POSITION_2);
        CreateTickets createTicketsCommand = createTickets(POSITION_1);

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
                        NoOperation.builder()
                                .sourceCommand(createTicketsCommand)
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
    public void bookTicketsCommandResultsInNoOperationException() throws Exception {
        createTickets(POSITION_1, POSITION_2);
        bookTickets(POSITION_1);
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
                                .build(),
                        TicketsAlreadyBooked.builder()
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

    @Test
    public void cancelTicketsCommandResultsInNoOperationException() throws Exception {
        createTickets(POSITION_1, POSITION_2);
        bookTickets(POSITION_1);
        CancelTickets cancelTicketsCommand = cancelTickets(POSITION_2);

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
                                .build(),
                        NoOperation.builder()
                                .sourceCommand(cancelTicketsCommand)
                                .build());
    }

    private BookTickets bookTickets(Position... positions) {
        BookTickets bookTicketsCommand = BookTickets.builder()
                .bookingId(BOOKING_ID)
                .addPositions(positions)
                .build();
        eventBus.post(bookTicketsCommand);
        return bookTicketsCommand;
    }

    private CreateTickets createTickets(Position... positions) {
        CreateTickets createTicketsCommand = CreateTickets.builder()
                .addPositions(positions)
                .build();
        eventBus.post(createTicketsCommand);
        return createTicketsCommand;
    }

    private CancelTickets cancelTickets(Position... positions) {
        CancelTickets cancelTicketsCommand = CancelTickets.builder()
                .bookingId(BOOKING_ID)
                .addPositions(positions)
                .build();
        eventBus.post(cancelTicketsCommand);
        return cancelTicketsCommand;
    }
}
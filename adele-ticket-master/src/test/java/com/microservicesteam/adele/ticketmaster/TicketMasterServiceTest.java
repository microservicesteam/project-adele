package com.microservicesteam.adele.ticketmaster;

import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.RESERVED;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.google.common.eventbus.EventBus;
import com.microservicesteam.adele.messaging.listeners.DeadEventListener;
import com.microservicesteam.adele.ticketmaster.commands.CancelReservation;
import com.microservicesteam.adele.ticketmaster.commands.CreateReservation;
import com.microservicesteam.adele.ticketmaster.commands.CreateTickets;
import com.microservicesteam.adele.ticketmaster.events.ReservationAccepted;
import com.microservicesteam.adele.ticketmaster.events.ReservationCancelled;
import com.microservicesteam.adele.ticketmaster.events.ReservationRejected;
import com.microservicesteam.adele.ticketmaster.events.TicketsCreated;
import com.microservicesteam.adele.ticketmaster.exceptions.NoOperation;
import com.microservicesteam.adele.ticketmaster.model.Reservation;
import com.microservicesteam.adele.ticketmaster.model.Ticket;
import com.microservicesteam.adele.ticketmaster.model.TicketId;

public class TicketMasterServiceTest {

    private static final String RESERVATION_ID = "abc-123";
    private static final TicketId TICKET_ID_1 = TicketId.builder().eventId(1).sectorId(1).seatId(1).build();
    private static final TicketId TICKET_ID_2 = TicketId.builder().eventId(1).sectorId(1).seatId(2).build();

    private TicketMasterService ticketMasterService;
    private EventBus eventBus;
    private DeadEventListener deadEventListener;

    @Before
    public void setUp() {
        eventBus = new EventBus();
        ticketMasterService = new TicketMasterService(eventBus);
        ticketMasterService.init();
        deadEventListener = new DeadEventListener(eventBus);
        deadEventListener.init();
    }

    @Test
    public void createTicketsCommandResultsInTicketsCreatedEvent() {
        //WHEN
        createTicketsForTickets(TICKET_ID_1, TICKET_ID_2);

        //THEN
        assertThat(ticketMasterService.ticketRepository)
                .hasSize(2)
                .contains(entry(TICKET_ID_1, FREE),
                        entry(TICKET_ID_2, FREE));
        assertThat(deadEventListener.deadEvents)
                .extracting("event")
                .containsExactly(ticketsCreatedEventForTickets(TICKET_ID_1, TICKET_ID_2));
    }

    @Test
    public void createTicketsCommandForAlreadyExistingTicketResultsInNoOperationEvent() {
        //GIVEN
        createTicketsForTickets(TICKET_ID_1, TICKET_ID_2);

        //WHEN
        CreateTickets ignoredCreateTicketsCommand = createTicketsForTickets(TICKET_ID_1);

        //THEN
        assertThat(ticketMasterService.ticketRepository)
                .hasSize(2)
                .contains(entry(TICKET_ID_1, FREE),
                        entry(TICKET_ID_2, FREE));
        assertThat(deadEventListener.deadEvents)
                .extracting("event")
                .containsExactly(
                        ticketsCreatedEventForTickets(TICKET_ID_1, TICKET_ID_2),
                        NoOperation.builder()
                                .sourceCommand(ignoredCreateTicketsCommand)
                                .build());
    }

    @Test
    public void createReservationCommandResultsInReservationCreatedEvent() {
        //GIVEN
        createTicketsForTickets(TICKET_ID_1, TICKET_ID_2);

        //WHEN
        createReservationForTickets(TICKET_ID_1);

        //THEN
        assertThat(ticketMasterService.ticketRepository)
                .hasSize(2)
                .contains(entry(TICKET_ID_1, RESERVED),
                        entry(TICKET_ID_2, FREE));
        assertThat(deadEventListener.deadEvents)
                .extracting("event")
                .containsExactly(
                        ticketsCreatedEventForTickets(TICKET_ID_1, TICKET_ID_2),
                        reservationAcceptedEventForTicket(TICKET_ID_1));
    }

    @Test
    public void createReservationForAlreadyReservedTicketResultsInReservationRejectedEvent() {
        //GIVEN
        createTicketsForTickets(TICKET_ID_1, TICKET_ID_2);
        createReservationForTickets(TICKET_ID_1);

        //WHEN
        createReservationForTickets(TICKET_ID_1);

        //THEN
        assertThat(ticketMasterService.ticketRepository)
                .hasSize(2)
                .contains(entry(TICKET_ID_1, RESERVED),
                        entry(TICKET_ID_2, FREE));
        assertThat(deadEventListener.deadEvents)
                .extracting("event")
                .containsExactly(
                        ticketsCreatedEventForTickets(TICKET_ID_1, TICKET_ID_2),
                        reservationAcceptedEventForTicket(TICKET_ID_1),
                        reservationRejectedEventForTicket(TICKET_ID_1));
    }

    @Test
    public void cancelReservationCommandResultsInReservationCancelledEvent() {
        //GIVEN
        createTicketsForTickets(TICKET_ID_1, TICKET_ID_2);
        createReservationForTickets(TICKET_ID_1);

        //WHEN
        cancelReservationForTickets(TICKET_ID_1);

        //THEN
        assertThat(ticketMasterService.ticketRepository)
                .hasSize(2)
                .contains(entry(TICKET_ID_1, FREE),
                        entry(TICKET_ID_2, FREE));
        assertThat(deadEventListener.deadEvents)
                .extracting("event")
                .containsExactly(
                        ticketsCreatedEventForTickets(TICKET_ID_1, TICKET_ID_2),
                        reservationAcceptedEventForTicket(TICKET_ID_1),
                        ReservationCancelled.builder()
                                .reservation(Reservation.builder()
                                        .reservationId(RESERVATION_ID)
                                        .addTickets(TICKET_ID_1)
                                        .build())
                                .build());
    }

    @Test
    public void cancelReservationCommandForFreeTicketResultsInNoOperationEvent() {
        //GIVEN
        createTicketsForTickets(TICKET_ID_1, TICKET_ID_2);
        createReservationForTickets(TICKET_ID_1);

        //WHEN
        CancelReservation cancelReservationCommand = cancelReservationForTickets(TICKET_ID_2);

        //THEN
        assertThat(ticketMasterService.ticketRepository)
                .hasSize(2)
                .contains(entry(TICKET_ID_1, RESERVED),
                        entry(TICKET_ID_2, FREE));
        assertThat(deadEventListener.deadEvents)
                .extracting("event")
                .containsExactly(
                        ticketsCreatedEventForTickets(TICKET_ID_1, TICKET_ID_2),
                        reservationAcceptedEventForTicket(TICKET_ID_1),
                        NoOperation.builder()
                                .sourceCommand(cancelReservationCommand)
                                .build());
    }

    private CreateReservation createReservationForTickets(TicketId... ticketIds) {
        CreateReservation createReservation = CreateReservation.builder()
                .reservation(Reservation.builder()
                        .reservationId(RESERVATION_ID)
                        .addTickets(ticketIds)
                        .build())
                .build();
        eventBus.post(createReservation);
        return createReservation;
    }

    private ReservationAccepted reservationAcceptedEventForTicket(TicketId ticketId) {
        return ReservationAccepted.builder()
                .reservation(Reservation.builder()
                        .reservationId(RESERVATION_ID)
                        .addTickets(ticketId)
                        .build())
                .build();
    }

    private ReservationRejected reservationRejectedEventForTicket(TicketId ticketId) {
        return ReservationRejected.builder()
                .reservation(Reservation.builder()
                        .reservationId(RESERVATION_ID)
                        .addTickets(ticketId)
                        .build())
                .build();
    }

    private CreateTickets createTicketsForTickets(TicketId... ticketIds) {
        CreateTickets createTicketsCommand = CreateTickets.builder()
                .addAllTickets(Arrays.stream(ticketIds)
                        .map(this::createFreeTicket)
                        .collect(toList()))
                .build();
        eventBus.post(createTicketsCommand);
        return createTicketsCommand;
    }

    private TicketsCreated ticketsCreatedEventForTickets(TicketId... ticketIds) {
        return TicketsCreated.builder()
                .addAllTickets(Arrays.stream(ticketIds)
                        .map(this::createFreeTicket)
                        .collect(toList()))
                .build();
    }

    private Ticket createFreeTicket(TicketId ticketId) {
        return Ticket.builder()
                .status(FREE)
                .ticketId(ticketId)
                .build();
    }

    private CancelReservation cancelReservationForTickets(TicketId... ticketIds) {
        CancelReservation cancelReservationCommand = CancelReservation.builder()
                .reservation(Reservation.builder()
                        .reservationId(RESERVATION_ID)
                        .addTickets(ticketIds)
                        .build())
                .build();
        eventBus.post(cancelReservationCommand);
        return cancelReservationCommand;
    }
}
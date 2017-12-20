package com.microservicesteam.adele.ticketmaster;

import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.BOOKED;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;
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
import com.microservicesteam.adele.ticketmaster.model.Position;
import com.microservicesteam.adele.ticketmaster.model.Reservation;
import com.microservicesteam.adele.ticketmaster.model.Ticket;

public class TicketMasterServiceTest {

    private static final String RESERVATION_ID = "abc-123";
    private static final Position POSITION_1 = Position.builder().eventId(1).sectorId(1).seatId(1).build();
    private static final Position POSITION_2 = Position.builder().eventId(1).sectorId(1).seatId(2).build();

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
        createTicketsForPositions(POSITION_1, POSITION_2);

        //THEN
        assertThat(ticketMasterService.ticketRepository)
                .hasSize(2)
                .contains(entry(POSITION_1, FREE),
                        entry(POSITION_2, FREE));
        assertThat(deadEventListener.deadEvents)
                .extracting("event")
                .containsExactly(ticketsCreatedEventForPositions(POSITION_1, POSITION_2));
    }

    @Test
    public void createTicketsCommandForAlreadyExistingPositionResultsInNoOperationEvent() {
        //GIVEN
        createTicketsForPositions(POSITION_1, POSITION_2);

        //WHEN
        CreateTickets ignoredCreateTicketsCommand = createTicketsForPositions(POSITION_1);

        //THEN
        assertThat(ticketMasterService.ticketRepository)
                .hasSize(2)
                .contains(entry(POSITION_1, FREE),
                        entry(POSITION_2, FREE));
        assertThat(deadEventListener.deadEvents)
                .extracting("event")
                .containsExactly(
                        ticketsCreatedEventForPositions(POSITION_1, POSITION_2),
                        NoOperation.builder()
                                .sourceCommand(ignoredCreateTicketsCommand)
                                .build());
    }

    @Test
    public void createReservationCommandResultsInReservationCreatedEvent() {
        //GIVEN
        createTicketsForPositions(POSITION_1, POSITION_2);

        //WHEN
        createReservationForPositions(POSITION_1);

        //THEN
        assertThat(ticketMasterService.ticketRepository)
                .hasSize(2)
                .contains(entry(POSITION_1, BOOKED),
                        entry(POSITION_2, FREE));
        assertThat(deadEventListener.deadEvents)
                .extracting("event")
                .containsExactly(
                        ticketsCreatedEventForPositions(POSITION_1, POSITION_2),
                        reservationAcceptedEventForPosition(POSITION_1));
    }

    @Test
    public void createReservationForAlreadyReservedPositionResultsInReservationRejectedEvent() {
        //GIVEN
        createTicketsForPositions(POSITION_1, POSITION_2);
        createReservationForPositions(POSITION_1);

        //WHEN
        createReservationForPositions(POSITION_1);

        //THEN
        assertThat(ticketMasterService.ticketRepository)
                .hasSize(2)
                .contains(entry(POSITION_1, BOOKED),
                        entry(POSITION_2, FREE));
        assertThat(deadEventListener.deadEvents)
                .extracting("event")
                .containsExactly(
                        ticketsCreatedEventForPositions(POSITION_1, POSITION_2),
                        reservationAcceptedEventForPosition(POSITION_1),
                        reservationRejectedEventForPosition(POSITION_1));
    }

    @Test
    public void cancelReservationCommandResultsInReservationCancelledEvent() {
        //GIVEN
        createTicketsForPositions(POSITION_1, POSITION_2);
        createReservationForPositions(POSITION_1);

        //WHEN
        cancelReservationForPositions(POSITION_1);

        //THEN
        assertThat(ticketMasterService.ticketRepository)
                .hasSize(2)
                .contains(entry(POSITION_1, FREE),
                        entry(POSITION_2, FREE));
        assertThat(deadEventListener.deadEvents)
                .extracting("event")
                .containsExactly(
                        ticketsCreatedEventForPositions(POSITION_1, POSITION_2),
                        reservationAcceptedEventForPosition(POSITION_1),
                        ReservationCancelled.builder()
                                .reservation(Reservation.builder()
                                        .reservationId(RESERVATION_ID)
                                        .addPositions(POSITION_1)
                                        .build())
                                .build());
    }

    @Test
    public void cancelReservationCommandForFreePositionResultsInNoOperationEvent() {
        //GIVEN
        createTicketsForPositions(POSITION_1, POSITION_2);
        createReservationForPositions(POSITION_1);

        //WHEN
        CancelReservation cancelReservationCommand = cancelReservationForPositions(POSITION_2);

        //THEN
        assertThat(ticketMasterService.ticketRepository)
                .hasSize(2)
                .contains(entry(POSITION_1, BOOKED),
                        entry(POSITION_2, FREE));
        assertThat(deadEventListener.deadEvents)
                .extracting("event")
                .containsExactly(
                        ticketsCreatedEventForPositions(POSITION_1, POSITION_2),
                        reservationAcceptedEventForPosition(POSITION_1),
                        NoOperation.builder()
                                .sourceCommand(cancelReservationCommand)
                                .build());
    }

    private CreateReservation createReservationForPositions(Position... positions) {
        CreateReservation createReservation = CreateReservation.builder()
                .reservation(Reservation.builder()
                        .reservationId(RESERVATION_ID)
                        .addPositions(positions)
                        .build())
                .build();
        eventBus.post(createReservation);
        return createReservation;
    }

    private ReservationAccepted reservationAcceptedEventForPosition(Position position) {
        return ReservationAccepted.builder()
                .reservation(Reservation.builder()
                        .reservationId(RESERVATION_ID)
                        .addPositions(position)
                        .build())
                .build();
    }

    private ReservationRejected reservationRejectedEventForPosition(Position position) {
        return ReservationRejected.builder()
                .reservation(Reservation.builder()
                        .reservationId(RESERVATION_ID)
                        .addPositions(position)
                        .build())
                .build();
    }

    private CreateTickets createTicketsForPositions(Position... positions) {
        CreateTickets createTicketsCommand = CreateTickets.builder()
                .addAllTickets(Arrays.stream(positions)
                        .map(this::createFreeTicket)
                        .collect(toList()))
                .build();
        eventBus.post(createTicketsCommand);
        return createTicketsCommand;
    }

    private TicketsCreated ticketsCreatedEventForPositions(Position... positions) {
        return TicketsCreated.builder()
                .addAllTickets(Arrays.stream(positions)
                        .map(this::createFreeTicket)
                        .collect(toList()))
                .build();
    }

    private Ticket createFreeTicket(Position position) {
        return Ticket.builder()
                .status(FREE)
                .position(position)
                .build();
    }

    private CancelReservation cancelReservationForPositions(Position... positions) {
        CancelReservation cancelReservationCommand = CancelReservation.builder()
                .reservation(Reservation.builder()
                        .reservationId(RESERVATION_ID)
                        .addPositions(positions)
                        .build())
                .build();
        eventBus.post(cancelReservationCommand);
        return cancelReservationCommand;
    }
}
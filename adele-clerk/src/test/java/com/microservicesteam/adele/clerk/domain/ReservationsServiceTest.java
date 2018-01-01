package com.microservicesteam.adele.clerk.domain;

import static com.microservicesteam.adele.clerk.domain.validator.ValidationResult.INVALID_POSITIONS_EMPTY;
import static com.microservicesteam.adele.clerk.domain.validator.ValidationResult.VALID_REQUEST;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.BOOKED;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.eventbus.EventBus;
import com.microservicesteam.adele.clerk.boundary.web.WebSocketEventPublisher;
import com.microservicesteam.adele.clerk.domain.validator.PositionsValidator;
import com.microservicesteam.adele.messaging.listeners.DeadEventListener;
import com.microservicesteam.adele.ticketmaster.commands.CreateReservation;
import com.microservicesteam.adele.ticketmaster.events.ReservationAccepted;
import com.microservicesteam.adele.ticketmaster.events.ReservationCancelled;
import com.microservicesteam.adele.ticketmaster.model.Position;
import com.microservicesteam.adele.ticketmaster.model.Reservation;
import com.microservicesteam.adele.ticketmaster.model.Ticket;

@RunWith(MockitoJUnitRunner.class)
public class ReservationsServiceTest {

    private static final long EVENT_ID = 1L;
    private static final int SECTOR_ID = 1;
    private static final Position POSITION_1 = Position.builder()
            .eventId(EVENT_ID)
            .sectorId(SECTOR_ID)
            .seatId(1)
            .build();
    private static final Position POSITION_2 = Position.builder()
            .eventId(EVENT_ID)
            .sectorId(SECTOR_ID)
            .seatId(2)
            .build();
    private static final String RESERVATION_ID = "abc-123";

    private ReservationsService underTest;
    private DeadEventListener deadEventListener;
    private EventBus eventBus;

    @Mock
    private PositionsValidator validator;
    @Mock
    private ReservationIdGenerator reservationIdGenerator;
    @Mock
    private WebSocketEventPublisher webSocketEventPublisher;
    @Mock
    private TicketRepository ticketRepository;

    @Before
    public void setUp() {
        eventBus = new EventBus();
        underTest = new ReservationsService(eventBus, ticketRepository, webSocketEventPublisher, validator, reservationIdGenerator);
        underTest.init();
        deadEventListener = new DeadEventListener(eventBus);
        deadEventListener.init();

        when(validator.validate(any(List.class))).thenReturn(VALID_REQUEST);
        when(reservationIdGenerator.generateReservationId()).thenReturn(RESERVATION_ID);
    }

    @Test
    public void reservePositionsShouldReturnReservationRejectionWhenValidationFails() {
        //given
        when(validator.validate(emptyList())).thenReturn(INVALID_POSITIONS_EMPTY);

        //when
        ReservationResponse reservationResponse = underTest.reservePositions(emptyList());

        //then
        assertThat(reservationResponse).isInstanceOf(ReservationRejected.class);
        ReservationRejected reservationRejected = (ReservationRejected) reservationResponse;
        assertThat(reservationRejected.code()).isEqualTo(INVALID_POSITIONS_EMPTY.code());
        assertThat(reservationRejected.reason()).isEqualTo(INVALID_POSITIONS_EMPTY.message());
    }

    @Test
    public void reservePositionsShouldGenerateReservationIdAndSendCreateReservationCommand() {
        //given
        List<Position> positions = Arrays.asList(POSITION_1, POSITION_2);

        //when
        ReservationResponse reservationResponse = underTest.reservePositions(positions);

        //then
        verify(validator).validate(positions);
        assertThat(reservationResponse).isInstanceOf(ReservationRequested.class);
        ReservationRequested reservationRequested = (ReservationRequested) reservationResponse;

        assertThat(reservationRequested.reservationId())
                .isEqualTo(RESERVATION_ID);
        assertThat(deadEventListener.deadEvents)
                .extracting("event")
                .containsExactly(CreateReservation.builder()
                        .reservation(Reservation.builder()
                                .reservationId(RESERVATION_ID)
                                .addPositions(POSITION_1, POSITION_2)
                                .build())
                        .build());
    }

    @Test
    public void onReservationAcceptedEventTicketRepositoryIsUpdatedAndEventIsPublished() {
        //given
        ReservationAccepted reservationAccepted = ReservationAccepted.builder()
                .reservation(Reservation.builder()
                        .reservationId(RESERVATION_ID)
                        .addPositions(POSITION_1)
                        .build())
                .build();

        //when
        eventBus.post(reservationAccepted);

        //then
        verify(ticketRepository).put(Ticket.builder()
                .status(BOOKED)
                .position(POSITION_1)
                .build());
        verify(webSocketEventPublisher).publishToSector(reservationAccepted);
    }

    @Test
    public void onReservationCancelledEventTicketRepositoryIsUpdatedAndEventIsPublished() {
        //given
        ReservationCancelled reservationCancelled = ReservationCancelled.builder()
                .reservation(Reservation.builder()
                        .reservationId(RESERVATION_ID)
                        .addPositions(POSITION_1, POSITION_2)
                        .build())
                .build();

        //when
        eventBus.post(reservationCancelled);

        //then
        verify(ticketRepository).put(Ticket.builder()
                .status(FREE)
                .position(POSITION_1)
                .build());
        verify(ticketRepository).put(Ticket.builder()
                .status(FREE)
                .position(POSITION_2)
                .build());
        verify(webSocketEventPublisher).publishToSector(reservationCancelled);
    }

    @Test
    public void onReservationRejectedEventTheEventIsPublished() {
        com.microservicesteam.adele.ticketmaster.events.ReservationRejected reservationRejected =
                com.microservicesteam.adele.ticketmaster.events.ReservationRejected.builder()
                .reservation(Reservation.builder()
                        .reservationId(RESERVATION_ID)
                        .addPositions(POSITION_1)
                        .build())
                .build();

        eventBus.post(reservationRejected);

        verify(webSocketEventPublisher).publishToSector(reservationRejected);
    }


}
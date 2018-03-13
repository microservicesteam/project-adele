package com.microservicesteam.adele.clerk.domain;

import static com.microservicesteam.adele.clerk.domain.validator.ValidationResult.INVALID_NO_TICKET;
import static com.microservicesteam.adele.clerk.domain.validator.ValidationResult.VALID_REQUEST;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.RESERVED;
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
import com.microservicesteam.adele.clerk.domain.validator.TicketValidator;
import com.microservicesteam.adele.messaging.listeners.DeadEventListener;
import com.microservicesteam.adele.ticketmaster.commands.CreateReservation;
import com.microservicesteam.adele.ticketmaster.events.ReservationAccepted;
import com.microservicesteam.adele.ticketmaster.events.ReservationCancelled;
import com.microservicesteam.adele.ticketmaster.model.Reservation;
import com.microservicesteam.adele.ticketmaster.model.Ticket;
import com.microservicesteam.adele.ticketmaster.model.TicketId;

@RunWith(MockitoJUnitRunner.class)
public class ReservationsServiceTest {

    private static final long PROGRAM_ID = 1L;
    private static final int SECTOR_ID = 1;
    private static final TicketId TICKET_ID_1 = TicketId.builder()
            .programId(PROGRAM_ID)
            .sectorId(SECTOR_ID)
            .seatId(1)
            .build();
    private static final TicketId TICKET_ID_2 = TicketId.builder()
            .programId(PROGRAM_ID)
            .sectorId(SECTOR_ID)
            .seatId(2)
            .build();
    private static final String RESERVATION_ID = "abc-123";

    private ReservationsService underTest;
    private DeadEventListener deadEventListener;
    private EventBus eventBus;

    @Mock
    private TicketValidator validator;
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
    public void reserveTicketsShouldReturnReservationRejectionWhenValidationFails() {
        //given
        when(validator.validate(emptyList())).thenReturn(INVALID_NO_TICKET);

        //when
        ReservationResponse reservationResponse = underTest.reserveTickets(emptyList());

        //then
        assertThat(reservationResponse).isInstanceOf(ReservationRejected.class);
        ReservationRejected reservationRejected = (ReservationRejected) reservationResponse;
        assertThat(reservationRejected.code()).isEqualTo(INVALID_NO_TICKET.code());
        assertThat(reservationRejected.reason()).isEqualTo(INVALID_NO_TICKET.message());
    }

    @Test
    public void reserveTicketsShouldGenerateReservationIdAndSendCreateReservationCommand() {
        //given
        List<TicketId> ticketIds = Arrays.asList(TICKET_ID_1, TICKET_ID_2);

        //when
        ReservationResponse reservationResponse = underTest.reserveTickets(ticketIds);

        //then
        verify(validator).validate(ticketIds);
        assertThat(reservationResponse).isInstanceOf(ReservationRequested.class);
        ReservationRequested reservationRequested = (ReservationRequested) reservationResponse;

        assertThat(reservationRequested.reservationId())
                .isEqualTo(RESERVATION_ID);
        assertThat(deadEventListener.deadEvents)
                .extracting("event")
                .containsExactly(CreateReservation.builder()
                        .reservation(Reservation.builder()
                                .reservationId(RESERVATION_ID)
                                .addTickets(TICKET_ID_1, TICKET_ID_2)
                                .build())
                        .build());
    }

    @Test
    public void onReservationAcceptedEventTicketRepositoryIsUpdatedAndEventIsPublished() {
        //given
        ReservationAccepted reservationAccepted = ReservationAccepted.builder()
                .reservation(Reservation.builder()
                        .reservationId(RESERVATION_ID)
                        .addTickets(TICKET_ID_1)
                        .build())
                .build();

        //when
        eventBus.post(reservationAccepted);

        //then
        verify(ticketRepository).put(Ticket.builder()
                .status(RESERVED)
                .ticketId(TICKET_ID_1)
                .build());
        verify(webSocketEventPublisher).publishToSector(reservationAccepted);
    }

    @Test
    public void onReservationCancelledEventTicketRepositoryIsUpdatedAndEventIsPublished() {
        //given
        ReservationCancelled reservationCancelled = ReservationCancelled.builder()
                .reservation(Reservation.builder()
                        .reservationId(RESERVATION_ID)
                        .addTickets(TICKET_ID_1, TICKET_ID_2)
                        .build())
                .build();

        //when
        eventBus.post(reservationCancelled);

        //then
        verify(ticketRepository).put(Ticket.builder()
                .status(FREE)
                .ticketId(TICKET_ID_1)
                .build());
        verify(ticketRepository).put(Ticket.builder()
                .status(FREE)
                .ticketId(TICKET_ID_2)
                .build());
        verify(webSocketEventPublisher).publishToSector(reservationCancelled);
    }

    @Test
    public void onReservationRejectedEventTheEventIsPublished() {
        com.microservicesteam.adele.ticketmaster.events.ReservationRejected reservationRejected =
                com.microservicesteam.adele.ticketmaster.events.ReservationRejected.builder()
                .reservation(Reservation.builder()
                        .reservationId(RESERVATION_ID)
                        .addTickets(TICKET_ID_1)
                        .build())
                .build();

        eventBus.post(reservationRejected);

        verify(webSocketEventPublisher).publishToSector(reservationRejected);
    }


}
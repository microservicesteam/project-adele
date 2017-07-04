package com.microservicesteam.adele.booking.domain;

import static com.microservicesteam.adele.booking.domain.validator.ValidationResult.INVALID_POSITIONS_EMPTY;
import static com.microservicesteam.adele.booking.domain.validator.ValidationResult.VALID_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import com.microservicesteam.adele.ticketmaster.events.TicketsAlreadyBooked;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.eventbus.EventBus;
import com.microservicesteam.adele.booking.boundary.web.WebSocketEventPublisher;
import com.microservicesteam.adele.booking.domain.validator.BookingRequestValidator;
import com.microservicesteam.adele.messaging.listeners.DeadEventListener;
import com.microservicesteam.adele.ticketmaster.commands.BookTickets;
import com.microservicesteam.adele.ticketmaster.events.TicketsBooked;
import com.microservicesteam.adele.ticketmaster.events.TicketsCancelled;
import com.microservicesteam.adele.ticketmaster.events.TicketsCreated;
import com.microservicesteam.adele.ticketmaster.model.BookedTicket;
import com.microservicesteam.adele.ticketmaster.model.FreeTicket;
import com.microservicesteam.adele.ticketmaster.model.Position;

@RunWith(MockitoJUnitRunner.class)
public class BookingServiceTest {

    private static final long EVENT_ID = 1L;
    private static final int SECTOR_ID = 1;
    private static final Position POSITION_1 = Position.builder()
            .eventId(EVENT_ID)
            .sectorId(SECTOR_ID)
            .id(1)
            .build();
    private static final Position POSITION_2 = Position.builder()
            .eventId(EVENT_ID)
            .sectorId(SECTOR_ID)
            .id(2)
            .build();
    private static final String BOOKING_ID = "abc-123";

    private BookingService bookingService;
    private DeadEventListener deadEventListener;
    private EventBus eventBus;

    @Mock
    private BookingRequestValidator validator;
    @Mock
    private BookingIdGenerator bookingIdGenerator;
    @Mock
    private WebSocketEventPublisher webSocketEventPublisher;

    @Before
    public void setUp() throws Exception {
        eventBus = new EventBus();
        bookingService = new BookingService(
                eventBus, validator, bookingIdGenerator, webSocketEventPublisher, new TicketRepository());
        bookingService.init();
        deadEventListener = new DeadEventListener(eventBus);
        deadEventListener.init();

        when(validator.validate(any(BookingRequest.class))).thenReturn(VALID_REQUEST);
        when(bookingIdGenerator.generateBookingId()).thenReturn(BOOKING_ID);
    }

    @Test
    public void onBookingRequestRequestIsValidated() throws Exception {
        //given
        BookingRequest bookingRequest = BookingRequest.builder()
                .eventId(1L)
                .sectorId(1)
                .build();
        when(validator.validate(bookingRequest)).thenReturn(INVALID_POSITIONS_EMPTY);

        //when
        BookingResponse bookingResponse = bookingService.bookTickets(bookingRequest);

        //then
        verify(validator, times(1)).validate(bookingRequest);
        assertThat(bookingResponse).isInstanceOf(BookingRejected.class);
        BookingRejected bookingRejected = (BookingRejected) bookingResponse;
        assertThat(bookingRejected.code()).isEqualTo(INVALID_POSITIONS_EMPTY.code());
        assertThat(bookingRejected.reason()).isEqualTo(INVALID_POSITIONS_EMPTY.message());
    }

    @Test
    public void onBookingRequestBookTicketsCommandCreatedAndSent() throws Exception {
        BookingRequest bookingRequest = BookingRequest.builder()
                .eventId(1L)
                .sectorId(1)
                .addPositions(1, 2)
                .build();
        BookingResponse bookingResponse = bookingService.bookTickets(bookingRequest);

        verify(validator, times(1)).validate(bookingRequest);
        assertThat(bookingResponse).isInstanceOf(BookingRequested.class);
        BookingRequested bookingRequested = (BookingRequested) bookingResponse;

        assertThat(bookingRequested.bookingId())
                .isEqualTo(BOOKING_ID);
        assertThat(deadEventListener.deadEvents)
                .extracting("event")
                .containsExactly(BookTickets.builder()
                        .bookingId(BOOKING_ID)
                        .addPositions(POSITION_1, POSITION_2)
                        .build());
    }

    @Test
    public void onTicketsCreatedMapIsUpdatedAndEventIsPublished() throws Exception {
        TicketsCreated ticketsCreated = TicketsCreated.builder()
                .addPositions(POSITION_1, POSITION_2)
                .build();

        eventBus.post(ticketsCreated);

        assertThat(bookingService.getTicketsStatus())
                .containsExactly(
                        FreeTicket.builder()
                                .position(POSITION_1)
                                .build(),
                        FreeTicket.builder()
                                .position(POSITION_2)
                                .build());
    }

    @Test
    public void onTicketsBookedMapIsUpdatedAndEventIsPublished() throws Exception {
        TicketsBooked ticketsBooked = TicketsBooked.builder()
                .bookingId(BOOKING_ID)
                .addPositions(POSITION_1, POSITION_2)
                .build();

        eventBus.post(ticketsBooked);

        assertThat(bookingService.getTicketsStatus())
                .containsExactly(
                        BookedTicket.builder()
                                .position(POSITION_1)
                                .bookingId(BOOKING_ID)
                                .build(),
                        BookedTicket.builder()
                                .position(POSITION_2)
                                .bookingId(BOOKING_ID)
                                .build());
        verify(webSocketEventPublisher).publish(ticketsBooked);
    }

    @Test
    public void onTicketsCancelledMapIsUpdatedAndEventIsPublished() throws Exception {
        TicketsCancelled ticketsCancelled = TicketsCancelled.builder()
                .bookingId(BOOKING_ID)
                .addPositions(POSITION_1, POSITION_2)
                .build();

        eventBus.post(ticketsCancelled);

        assertThat(bookingService.getTicketsStatus())
                .containsExactly(
                        FreeTicket.builder()
                                .position(POSITION_1)
                                .build(),
                        FreeTicket.builder()
                                .position(POSITION_2)
                                .build());
        verify(webSocketEventPublisher).publish(ticketsCancelled);
    }

    @Test
    public void onTicketsWereAlreadyBookedEventIsPublished() {
        TicketsAlreadyBooked ticketsAlreadyBooked = TicketsAlreadyBooked.builder()
                .bookingId(BOOKING_ID)
                .build();

        eventBus.post(ticketsAlreadyBooked);

        verify(webSocketEventPublisher).publish(ticketsAlreadyBooked);
    }

    @Test
    public void getTicketsStatusReturnsListOfTickets() throws Exception {
        TicketsCreated ticketsCreated = TicketsCreated.builder()
                .addPositions(POSITION_1, POSITION_2)
                .build();
        TicketsBooked ticketsBooked = TicketsBooked.builder()
                .bookingId(BOOKING_ID)
                .addPositions(POSITION_1)
                .build();

        eventBus.post(ticketsCreated);
        eventBus.post(ticketsBooked);

        assertThat(bookingService.getTicketsStatus())
                .containsExactly(
                        BookedTicket.builder()
                                .position(POSITION_1)
                                .bookingId(BOOKING_ID)
                                .build(),
                        FreeTicket.builder()
                                .position(POSITION_2)
                                .build());
    }
}
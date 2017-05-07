package com.microservicesteam.adele.booking.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.eventbus.EventBus;
import com.microservicesteam.adele.booking.boundary.web.EventPublisher;
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
    private static final Position POSITION_2 = Position.builder()
            .eventId(EVENT_ID)
            .sectorId(SECTOR_ID)
            .id(2)
            .build();
    private static final Position POSITION_1 = Position.builder()
            .eventId(EVENT_ID)
            .sectorId(SECTOR_ID)
            .id(1)
            .build();
    private static final String BOOKING_ID = "abc-123";

    private BookingService bookingService;
    private DeadEventListener deadEventListener;
    private EventBus eventBus;

    @Mock
    private BookingIdGenerator bookingIdGenerator;
    @Mock
    private EventPublisher eventPublisher;


    @Before
    public void setUp() throws Exception {
        eventBus = new EventBus();
        bookingService = new BookingService(eventBus, bookingIdGenerator, eventPublisher);
        bookingService.init();
        deadEventListener = new DeadEventListener(eventBus);
        deadEventListener.init();

        when(bookingIdGenerator.generateBookingId()).thenReturn(BOOKING_ID);
    }

    @Test
    public void onBookingRequestBookTicketsCommandCreatedAndSent() throws Exception {
        BookingResponse bookingResponse = bookingService.bookTickets(BookingRequest.builder()
                .withEventId(1L)
                .withSectorId(1)
                .addPositions(1, 2)
                .build());

        assertThat(bookingResponse.bookingId())
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

        assertThat(bookingService.ticketRepository)
                .containsExactly(
                        entry(POSITION_1, FreeTicket.builder()
                                .position(POSITION_1)
                                .build()),
                        entry(POSITION_2, FreeTicket.builder()
                                .position(POSITION_2)
                                .build()));
        verify(eventPublisher).publish(ticketsCreated);
    }

    @Test
    public void onTicketsBookedMapIsUpdatedAndEventIsPublished() throws Exception {
        TicketsBooked ticketsBooked = TicketsBooked.builder()
                .bookingId(BOOKING_ID)
                .addPositions(POSITION_1, POSITION_2)
                .build();

        eventBus.post(ticketsBooked);

        assertThat(bookingService.ticketRepository)
                .containsExactly(
                        entry(POSITION_1, BookedTicket.builder()
                                .position(POSITION_1)
                                .bookingId(BOOKING_ID)
                                .build()),
                        entry(POSITION_2, BookedTicket.builder()
                                .position(POSITION_2)
                                .bookingId(BOOKING_ID)
                                .build()));
        verify(eventPublisher).publish(ticketsBooked);
    }

    @Test
    public void onTicketsCancelledMapIsUpdatedAndEventIsPublished() throws Exception {
        TicketsCancelled ticketsCancelled = TicketsCancelled.builder()
                .bookingId(BOOKING_ID)
                .addPositions(POSITION_1, POSITION_2)
                .build();

        eventBus.post(ticketsCancelled);

        assertThat(bookingService.ticketRepository)
                .containsExactly(
                        entry(POSITION_1, FreeTicket.builder()
                                .position(POSITION_1)
                                .build()),
                        entry(POSITION_2, FreeTicket.builder()
                                .position(POSITION_2)
                                .build()));
        verify(eventPublisher).publish(ticketsCancelled);
    }
}
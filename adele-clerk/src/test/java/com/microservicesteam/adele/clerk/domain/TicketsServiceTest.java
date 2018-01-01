package com.microservicesteam.adele.clerk.domain;

import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.BOOKED;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.microservicesteam.adele.messaging.listeners.DeadEventListener;
import com.microservicesteam.adele.ticketmaster.events.TicketsCreated;
import com.microservicesteam.adele.ticketmaster.model.Position;
import com.microservicesteam.adele.ticketmaster.model.Ticket;

@RunWith(MockitoJUnitRunner.class)
public class TicketsServiceTest {

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

    private TicketsService underTest;
    private DeadEventListener deadEventListener;
    private EventBus eventBus;

    @Mock
    private TicketRepository ticketRepository;

    @Before
    public void setUp() {
        eventBus = new EventBus();
        deadEventListener = new DeadEventListener(eventBus);
        deadEventListener.init();

        underTest = new TicketsService(eventBus, ticketRepository);
        underTest.init();
    }

    @Test
    public void onTicketsCreatedMapIsUpdatedAndEventIsPublished() {
        //given
        TicketsCreated ticketsCreated = TicketsCreated.builder()
                .addTickets(Ticket.builder()
                                .status(FREE)
                                .position(POSITION_1)
                                .build(),
                        Ticket.builder()
                                .status(FREE)
                                .position(POSITION_2)
                                .build())
                .build();

        //when
        eventBus.post(ticketsCreated);

        //then
        verify(ticketRepository).put(Ticket.builder()
                .status(FREE)
                .position(POSITION_1)
                .build());
        verify(ticketRepository).put(Ticket.builder()
                .status(FREE)
                .position(POSITION_2)
                .build());
    }

    @Test
    public void getTicketsStatusReturnsListOfTickets() {
        ImmutableList<Ticket> ticketsInRepository = ImmutableList.of(
                Ticket.builder()
                        .status(BOOKED)
                        .position(POSITION_1)
                        .build(),
                Ticket.builder()
                        .status(FREE)
                        .position(POSITION_2)
                        .build());
        when(ticketRepository.getTicketsStatusByEventAndSector(1, SECTOR_ID)).thenReturn(ticketsInRepository);
        assertThat(underTest.getTicketsStatusByEventAndSector(1, SECTOR_ID))
                .isEqualTo(ticketsInRepository);
    }
}
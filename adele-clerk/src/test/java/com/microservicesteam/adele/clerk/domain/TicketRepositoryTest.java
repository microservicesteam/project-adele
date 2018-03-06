package com.microservicesteam.adele.clerk.domain;

import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.RESERVED;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.microservicesteam.adele.ticketmaster.model.Position;
import com.microservicesteam.adele.ticketmaster.model.Ticket;

public class TicketRepositoryTest {

    private static final int ID_1 = 0;
    private static final int ID_2 = 1;
    private static final int EVENT_ID = 1;
    private static final int SECTOR_ID = 2;
    private static final Position POSITION_1 = Position.builder()
            .seatId(ID_1)
            .eventId(EVENT_ID)
            .sectorId(SECTOR_ID)
            .build();

    private static final Position POSITION_2 = Position.builder()
            .seatId(ID_2)
            .eventId(EVENT_ID)
            .sectorId(SECTOR_ID)
            .build();

    private static final Ticket FREE_TICKET = Ticket.builder()
            .status(FREE)
            .position(POSITION_1)
            .build();

    private TicketRepository underTest;

    @Before
    public void setUp() {
        underTest = new TicketRepository();
    }

    @Test
    public void hasShouldReturnTicketPreviouslyPutIn() {
        underTest.put(FREE_TICKET);
        assertThat(underTest.has(POSITION_1)).isTrue();
    }

    @Test
    public void hasShouldNotRelyOnObjectIdentity() {
        underTest.put(FREE_TICKET);
        Position copyPosition = Position.builder()
                .seatId(ID_1)
                .sectorId(SECTOR_ID)
                .eventId(EVENT_ID)
                .build();
        assertThat(underTest.has(copyPosition)).isTrue();
    }

    @Test
    public void hasShouldNotReturnTicketPreviouslyNotPutIn() {
        underTest.put(FREE_TICKET);
        assertThat(underTest.has(POSITION_2)).isFalse();
    }

    @Test
    public void hasShouldWorkOnEmptyRepository() {
        assertThat(underTest.has(POSITION_1)).isFalse();
        assertThat(underTest.has(POSITION_2)).isFalse();
    }

    @Test
    public void getShouldReturnTicketPreviouslyPutIn() {
        underTest.put(FREE_TICKET);
        assertThat(underTest.get(POSITION_1)).isEqualTo(FREE_TICKET);
    }

    @Test
    public void getShouldNotRelyOnObjectIdentity() {
        underTest.put(FREE_TICKET);
        Position copyPosition = Position.builder()
                .seatId(ID_1)
                .sectorId(SECTOR_ID)
                .eventId(EVENT_ID)
                .build();
        assertThat(underTest.get(copyPosition)).isEqualTo(FREE_TICKET);
    }

    @Test
    public void getShouldNotReturnTicketPreviouslyNotPutIn() {
        underTest.put(FREE_TICKET);
        assertThat(underTest.get(POSITION_2)).isNull();
    }

    @Test
    public void getShouldWorkOnEmptyRepository() {
        assertThat(underTest.get(POSITION_1)).isNull();
        assertThat(underTest.get(POSITION_2)).isNull();
    }

    @Test
    public void getShouldReturnTheLastTicketPutForTheSamePosition() {
        underTest.put(FREE_TICKET);
        Ticket reservedTicket = Ticket.builder()
                .status(RESERVED)
                .position(FREE_TICKET.position())
                .build();
        underTest.put(reservedTicket);
        assertThat(underTest.get(POSITION_1)).isEqualTo(reservedTicket);
    }

    @Test
    public void getTicketsStatusShouldReturnEmptyListOnEmptyRepository() {
        assertThat(underTest.getTicketsStatusByEvent(EVENT_ID)).isEmpty();
    }

    @Test
    public void getTicketsStatusShouldOnlyReturnTicketsForTheGivenEventIdAndSector() {
        Ticket freeTicketOnOtherEvent = Ticket.builder()
                .status(FREE)
                .position(Position.builder()
                        .seatId(ID_1)
                        .eventId(2)
                        .sectorId(SECTOR_ID)
                        .build())
                .build();
        underTest.put(FREE_TICKET);
        underTest.put(freeTicketOnOtherEvent);
        assertThat(underTest.getTicketsStatusByEvent(EVENT_ID)).containsExactly(FREE_TICKET);
    }

    @Test
    public void getTicketsStatusShouldReturnAllTicketsForTheGivenEventId() {
        Ticket freeTicketOnOtherSector = Ticket.builder()
                .status(FREE)
                .position(Position.builder()
                        .seatId(ID_1)
                        .eventId(EVENT_ID)
                        .sectorId(3)
                        .build())
                .build();
        underTest.put(FREE_TICKET);
        underTest.put(freeTicketOnOtherSector);
        assertThat(underTest.getTicketsStatusByEvent(EVENT_ID)).containsExactly(FREE_TICKET, freeTicketOnOtherSector);
    }
}
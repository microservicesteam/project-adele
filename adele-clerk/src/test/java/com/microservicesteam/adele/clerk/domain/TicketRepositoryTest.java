package com.microservicesteam.adele.clerk.domain;

import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.RESERVED;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.microservicesteam.adele.ticketmaster.model.Ticket;
import com.microservicesteam.adele.ticketmaster.model.TicketId;

public class TicketRepositoryTest {

    private static final int ID_1 = 0;
    private static final int ID_2 = 1;
    private static final int PROGRAM_ID = 1;
    private static final int SECTOR_ID = 2;
    private static final TicketId TICKET_ID_1 = TicketId.builder()
            .seatId(ID_1)
            .programId(PROGRAM_ID)
            .sectorId(SECTOR_ID)
            .build();

    private static final TicketId TICKET_ID_2 = TicketId.builder()
            .seatId(ID_2)
            .programId(PROGRAM_ID)
            .sectorId(SECTOR_ID)
            .build();

    private static final Ticket FREE_TICKET = Ticket.builder()
            .status(FREE)
            .ticketId(TICKET_ID_1)
            .build();

    private TicketRepository underTest;

    @Before
    public void setUp() {
        underTest = new TicketRepository();
    }

    @Test
    public void hasShouldReturnTicketPreviouslyPutIn() {
        underTest.put(FREE_TICKET);
        assertThat(underTest.has(TICKET_ID_1)).isTrue();
    }

    @Test
    public void hasShouldNotRelyOnObjectIdentity() {
        underTest.put(FREE_TICKET);
        TicketId copyTicketId = TicketId.builder()
                .seatId(ID_1)
                .sectorId(SECTOR_ID)
                .programId(PROGRAM_ID)
                .build();
        assertThat(underTest.has(copyTicketId)).isTrue();
    }

    @Test
    public void hasShouldNotReturnTicketPreviouslyNotPutIn() {
        underTest.put(FREE_TICKET);
        assertThat(underTest.has(TICKET_ID_2)).isFalse();
    }

    @Test
    public void hasShouldWorkOnEmptyRepository() {
        assertThat(underTest.has(TICKET_ID_1)).isFalse();
        assertThat(underTest.has(TICKET_ID_2)).isFalse();
    }

    @Test
    public void getShouldReturnTicketPreviouslyPutIn() {
        underTest.put(FREE_TICKET);
        assertThat(underTest.get(TICKET_ID_1)).isEqualTo(FREE_TICKET);
    }

    @Test
    public void getShouldNotRelyOnObjectIdentity() {
        underTest.put(FREE_TICKET);
        TicketId copyTicketId = TicketId.builder()
                .seatId(ID_1)
                .sectorId(SECTOR_ID)
                .programId(PROGRAM_ID)
                .build();
        assertThat(underTest.get(copyTicketId)).isEqualTo(FREE_TICKET);
    }

    @Test
    public void getShouldNotReturnTicketPreviouslyNotPutIn() {
        underTest.put(FREE_TICKET);
        assertThat(underTest.get(TICKET_ID_2)).isNull();
    }

    @Test
    public void getShouldWorkOnEmptyRepository() {
        assertThat(underTest.get(TICKET_ID_1)).isNull();
        assertThat(underTest.get(TICKET_ID_2)).isNull();
    }

    @Test
    public void getShouldReturnTheLastTicketPutForTheSameTicketId() {
        underTest.put(FREE_TICKET);
        Ticket reservedTicket = Ticket.builder()
                .status(RESERVED)
                .ticketId(FREE_TICKET.ticketId())
                .build();
        underTest.put(reservedTicket);
        assertThat(underTest.get(TICKET_ID_1)).isEqualTo(reservedTicket);
    }

    @Test
    public void getTicketsStatusShouldReturnEmptyListOnEmptyRepository() {
        assertThat(underTest.getTicketsStatusByProgram(PROGRAM_ID)).isEmpty();
    }

    @Test
    public void getTicketsStatusShouldOnlyReturnTicketsForTheGivenProgramIdAndSector() {
        Ticket freeTicketOnOtherProgram = Ticket.builder()
                .status(FREE)
                .ticketId(TicketId.builder()
                        .seatId(ID_1)
                        .programId(2)
                        .sectorId(SECTOR_ID)
                        .build())
                .build();
        underTest.put(FREE_TICKET);
        underTest.put(freeTicketOnOtherProgram);
        assertThat(underTest.getTicketsStatusByProgram(PROGRAM_ID)).containsExactly(FREE_TICKET);
    }

    @Test
    public void getTicketsStatusShouldReturnAllTicketsForTheGivenProgramId() {
        Ticket freeTicketOnOtherSector = Ticket.builder()
                .status(FREE)
                .ticketId(TicketId.builder()
                        .seatId(ID_1)
                        .programId(PROGRAM_ID)
                        .sectorId(3)
                        .build())
                .build();
        underTest.put(FREE_TICKET);
        underTest.put(freeTicketOnOtherSector);
        assertThat(underTest.getTicketsStatusByProgram(PROGRAM_ID)).containsExactly(FREE_TICKET, freeTicketOnOtherSector);
    }
}
package com.microservicesteam.adele.clerk.domain.validator;

import static com.microservicesteam.adele.clerk.domain.validator.ValidationResult.INVALID_NO_TICKET;
import static com.microservicesteam.adele.clerk.domain.validator.ValidationResult.INVALID_TICKETS_OUT_OF_SECTOR;
import static com.microservicesteam.adele.clerk.domain.validator.ValidationResult.INVALID_TICKETS_RESERVED;
import static com.microservicesteam.adele.clerk.domain.validator.ValidationResult.VALID_REQUEST;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.RESERVED;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.SOLD;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.microservicesteam.adele.clerk.domain.TicketRepository;
import com.microservicesteam.adele.ticketmaster.model.Ticket;
import com.microservicesteam.adele.ticketmaster.model.TicketId;

public class TicketValidatorTest {

    private TicketValidator validator;

    @Before
    public void setUp() {
        TicketRepository ticketRepository = new TicketRepository();

        ticketRepository.put(Ticket.builder()
                .ticketId(ticketId(1))
                .status(FREE)
                .build());
        ticketRepository.put(Ticket.builder()
                .ticketId(ticketId(2))
                .status(FREE)
                .build());
        ticketRepository.put(Ticket.builder()
                .ticketId(ticketId(3))
                .status(FREE)
                .build());
        ticketRepository.put(Ticket.builder()
                .ticketId(ticketId(4))
                .status(RESERVED)
                .build());
        ticketRepository.put(Ticket.builder()
                .ticketId(ticketId(5))
                .status(SOLD)
                .build());

        validator = new TicketValidator(ticketRepository);
    }

    @Test
    public void validWhenTicketsAreFree() {
        //given
        List<TicketId> ticketIds = Arrays.asList(ticketId(1), ticketId(2), ticketId(3));

        //when
        ValidationResult actual = validator.validate(ticketIds);

        //then
        assertThat(actual).isEqualTo(VALID_REQUEST);
    }

    @Test
    public void invalidWhenTicketsIsEmpty() {
        //given
        List<TicketId> ticketIds = emptyList();

        //when
        ValidationResult actual = validator.validate(ticketIds);

        //then
        assertThat(actual).isEqualTo(INVALID_NO_TICKET);
    }


    @Test
    public void invalidWhenHasTicketOutOfSector() {
        //given
        List<TicketId> ticketIds = Arrays.asList(ticketId(1), ticketId(10));

        //when
        ValidationResult actual = validator.validate(ticketIds);

        //then
        assertThat(actual).isEqualTo(INVALID_TICKETS_OUT_OF_SECTOR);
    }

    @Test
    public void invalidWhenHasReservedTicket() {
        //given
        List<TicketId> ticketIds = Arrays.asList(ticketId(1), ticketId(4));

        //when
        ValidationResult actual = validator.validate(ticketIds);

        //then
        assertThat(actual).isEqualTo(INVALID_TICKETS_RESERVED);
    }

    @Test
    public void invalidWhenHasSoldTicket() {
        //given
        List<TicketId> ticketIds = Arrays.asList(ticketId(1), ticketId(5));

        //when
        ValidationResult actual = validator.validate(ticketIds);

        //then
        assertThat(actual).isEqualTo(INVALID_TICKETS_RESERVED);
    }

    private TicketId ticketId(int seatId) {
        return TicketId.builder()
                .programId(1)
                .sectorId(1)
                .seatId(seatId)
                .build();
    }

}
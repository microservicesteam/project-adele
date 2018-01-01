package com.microservicesteam.adele.clerk.domain.validator;

import static com.microservicesteam.adele.clerk.domain.validator.ValidationResult.INVALID_POSITIONS_BOOKED;
import static com.microservicesteam.adele.clerk.domain.validator.ValidationResult.INVALID_POSITIONS_EMPTY;
import static com.microservicesteam.adele.clerk.domain.validator.ValidationResult.INVALID_POSITIONS_OUT_OF_SECTOR;
import static com.microservicesteam.adele.clerk.domain.validator.ValidationResult.VALID_REQUEST;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.BOOKED;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.PAID;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.microservicesteam.adele.clerk.domain.TicketRepository;
import com.microservicesteam.adele.ticketmaster.model.Position;
import com.microservicesteam.adele.ticketmaster.model.Ticket;

public class PositionsValidatorTest {

    private PositionsValidator validator;

    @Before
    public void setUp() {
        TicketRepository ticketRepository = new TicketRepository();

        ticketRepository.put(Ticket.builder()
                .position(position(1))
                .status(FREE)
                .build());
        ticketRepository.put(Ticket.builder()
                .position(position(2))
                .status(FREE)
                .build());
        ticketRepository.put(Ticket.builder()
                .position(position(3))
                .status(FREE)
                .build());
        ticketRepository.put(Ticket.builder()
                .position(position(4))
                .status(BOOKED)
                .build());
        ticketRepository.put(Ticket.builder()
                .position(position(5))
                .status(PAID)
                .build());

        validator = new PositionsValidator(ticketRepository);
    }

    @Test
    public void validWhenTicketsAreFree() {
        //given
        List<Position> positions = Arrays.asList(position(1), position(2), position(3));

        //when
        ValidationResult actual = validator.validate(positions);

        //then
        assertThat(actual).isEqualTo(VALID_REQUEST);
    }

    @Test
    public void invalidWhenPositionsIsEmpty() {
        //given
        List<Position> positions = emptyList();

        //when
        ValidationResult actual = validator.validate(positions);

        //then
        assertThat(actual).isEqualTo(INVALID_POSITIONS_EMPTY);
    }


    @Test
    public void invalidWhenHasPositionOutOfSector() {
        //given
        List<Position> positions = Arrays.asList(position(1), position(10));

        //when
        ValidationResult actual = validator.validate(positions);

        //then
        assertThat(actual).isEqualTo(INVALID_POSITIONS_OUT_OF_SECTOR);
    }

    @Test
    public void invalidWhenHasBookedPosition() {
        //given
        List<Position> positions = Arrays.asList(position(1), position(4));

        //when
        ValidationResult actual = validator.validate(positions);

        //then
        assertThat(actual).isEqualTo(INVALID_POSITIONS_BOOKED);
    }

    @Test
    public void invalidWhenHasPaidPosition() throws Exception {
        //given
        List<Position> positions = Arrays.asList(position(1), position(5));

        //when
        ValidationResult actual = validator.validate(positions);

        //then
        assertThat(actual).isEqualTo(INVALID_POSITIONS_BOOKED);
    }

    private Position position(int position) {
        return Position.builder()
                .eventId(1)
                .sectorId(1)
                .seatId(position)
                .build();
    }

}
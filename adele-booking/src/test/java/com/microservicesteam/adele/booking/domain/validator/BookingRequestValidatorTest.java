package com.microservicesteam.adele.booking.domain.validator;

import static com.microservicesteam.adele.booking.domain.validator.ValidationResult.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.microservicesteam.adele.booking.domain.BookingRequest;
import com.microservicesteam.adele.booking.domain.TicketRepository;
import com.microservicesteam.adele.ticketmaster.model.BookedTicket;
import com.microservicesteam.adele.ticketmaster.model.FreeTicket;
import com.microservicesteam.adele.ticketmaster.model.PaidTicket;
import com.microservicesteam.adele.ticketmaster.model.Position;

public class BookingRequestValidatorTest {

    private BookingRequestValidator validator;

    @Before
    public void setUp() throws Exception {
        TicketRepository ticketRepository = new TicketRepository();

        ticketRepository.put(FreeTicket.builder()
                .position(position(1))
                .build());
        ticketRepository.put(FreeTicket.builder()
                .position(position(2))
                .build());
        ticketRepository.put(FreeTicket.builder()
                .position(position(3))
                .build());
        ticketRepository.put(BookedTicket.builder()
                .bookingId("abc")
                .position(position(4))
                .build());
        ticketRepository.put(PaidTicket.builder()
                .bookingId("abc")
                .position(position(5))
                .build());

        validator = new BookingRequestValidator(ticketRepository);
    }

    @Test
    public void validWhenTicketsAreFree() throws Exception {
        //given
        BookingRequest bookingRequest = BookingRequest.builder()
                .withEventId(1)
                .withSectorId(1)
                .addPositions(1, 2, 3)
                .build();

        //when
        ValidationResult actual = validator.validate(bookingRequest);

        //then
        assertThat(actual).isEqualTo(VALID_REQUEST);
    }

    @Test
    public void invalidWhenPositionsIsEmpty() throws Exception {
        //given
        BookingRequest bookingRequest = BookingRequest.builder()
                .withEventId(1)
                .withSectorId(1)
                .build();

        //when
        ValidationResult actual = validator.validate(bookingRequest);

        //then
        assertThat(actual).isEqualTo(INVALID_POSITIONS_EMPTY);
    }

    @Test
    public void invalidWhenHasBookedPosition() throws Exception {
        //given
        BookingRequest bookingRequest = BookingRequest.builder()
                .withEventId(1)
                .withSectorId(1)
                .addPositions(1, 4)
                .build();

        //when
        ValidationResult actual = validator.validate(bookingRequest);

        //then
        assertThat(actual).isEqualTo(INVALID_POSITIONS_BOOKED);
    }

    @Test
    public void invalidWhenHasPaidPosition() throws Exception {
        //given
        BookingRequest bookingRequest = BookingRequest.builder()
                .withEventId(1)
                .withSectorId(1)
                .addPositions(1, 5)
                .build();

        //when
        ValidationResult actual = validator.validate(bookingRequest);

        //then
        assertThat(actual).isEqualTo(INVALID_POSITIONS_BOOKED);
    }

    private Position position(int position) {
        return Position.builder()
                .eventId(1)
                .sectorId(1)
                .id(position)
                .build();
    }

}
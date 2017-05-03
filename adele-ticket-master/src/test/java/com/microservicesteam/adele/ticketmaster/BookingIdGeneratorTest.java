package com.microservicesteam.adele.ticketmaster;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class BookingIdGeneratorTest {

    private BookingIdGenerator bookingIdGenerator;

    @Before
    public void setUp() throws Exception {
        bookingIdGenerator = new BookingIdGenerator();
    }

    @Test
    public void bookingIdIsRandom() throws Exception {
        String bookingId_1 = bookingIdGenerator.generateBookingId();
        String bookingId_2 = bookingIdGenerator.generateBookingId();

        assertThat(bookingId_1).isNotEqualTo(bookingId_2);
    }
}
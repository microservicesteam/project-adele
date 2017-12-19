package com.microservicesteam.adele.booking.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class ReservationIdGeneratorTest {

    private ReservationIdGenerator reservationIdGenerator;

    @Before
    public void setUp() throws Exception {
        reservationIdGenerator = new ReservationIdGenerator();
    }

    @Test
    public void bookingIdIsUnique() throws Exception {
        String bookingId_1 = reservationIdGenerator.generateReservationId();
        String bookingId_2 = reservationIdGenerator.generateReservationId();

        assertThat(bookingId_1).isNotEqualTo(bookingId_2);
    }
}
package com.microservicesteam.adele.clerk.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class ReservationIdGeneratorTest {

    private ReservationIdGenerator reservationIdGenerator;

    @Before
    public void setUp() {
        reservationIdGenerator = new ReservationIdGenerator();
    }

    @Test
    public void bookingIdIsUnique() {
        String bookingId_1 = reservationIdGenerator.generateReservationId();
        String bookingId_2 = reservationIdGenerator.generateReservationId();

        assertThat(bookingId_1).isNotEqualTo(bookingId_2);
    }
}
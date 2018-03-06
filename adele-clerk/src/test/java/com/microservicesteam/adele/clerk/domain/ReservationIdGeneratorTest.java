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
    public void reservationIdsAreUnique() {
        String reservationId1 = reservationIdGenerator.generateReservationId();
        String reservationId2 = reservationIdGenerator.generateReservationId();

        assertThat(reservationId1).isNotEqualTo(reservationId2);
    }
}
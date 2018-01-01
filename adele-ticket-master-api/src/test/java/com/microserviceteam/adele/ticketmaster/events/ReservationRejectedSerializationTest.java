package com.microserviceteam.adele.ticketmaster.events;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.boot.test.json.JsonContent;

import com.microservicesteam.adele.ticketmaster.events.Event;
import com.microservicesteam.adele.ticketmaster.events.ReservationRejected;
import com.microservicesteam.adele.ticketmaster.model.Position;
import com.microservicesteam.adele.ticketmaster.model.Reservation;

public class ReservationRejectedSerializationTest extends AbstractSerializationTest {

    @Test
    public void serialize() throws Exception {
        ReservationRejected reservationRejected = ReservationRejected.builder()
                .reservation(Reservation.builder()
                        .reservationId("abc-123")
                        .addPositions(
                                Position.builder()
                                        .eventId(1L)
                                        .sectorId(2)
                                        .seatId(3)
                                        .build())
                        .build())
                .build();

        JsonContent<Event> serializedJson = json.write(reservationRejected);
        assertThat(serializedJson).extractingJsonPathStringValue("type").isEqualTo("ReservationRejected");
        assertThat(serializedJson).extractingJsonPathStringValue("$.reservation.reservationId").isEqualTo("abc-123");
        assertThat(serializedJson).extractingJsonPathNumberValue("$.reservation.positions[0].eventId").isEqualTo(1);
        assertThat(serializedJson).extractingJsonPathNumberValue("$.reservation.positions[0].sectorId").isEqualTo(2);
        assertThat(serializedJson).extractingJsonPathNumberValue("$.reservation.positions[0].seatId").isEqualTo(3);

        assertThat(json.parse(serializedJson.getJson()))
                .isEqualTo(reservationRejected);
    }
}

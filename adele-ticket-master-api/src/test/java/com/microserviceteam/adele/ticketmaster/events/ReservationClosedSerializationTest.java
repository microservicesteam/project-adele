package com.microserviceteam.adele.ticketmaster.events;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.springframework.boot.test.json.JsonContent;

import com.microservicesteam.adele.ticketmaster.events.Event;
import com.microservicesteam.adele.ticketmaster.events.ReservationClosed;
import com.microservicesteam.adele.ticketmaster.model.Reservation;
import com.microservicesteam.adele.ticketmaster.model.TicketId;

public class ReservationClosedSerializationTest extends AbstractSerializationTest {

    @Test
    public void serialize() throws IOException {
        ReservationClosed reservationClosed = ReservationClosed.builder()
                .reservation(Reservation.builder()
                        .reservationId("abc-123")
                        .addTickets(TicketId.builder()
                                .programId(1L)
                                .sectorId(2)
                                .seatId(3)
                                .build())
                        .build())
                .build();

        JsonContent<Event> serializedJson = json.write(reservationClosed);
        assertThat(serializedJson).extractingJsonPathStringValue("type").isEqualTo("ReservationClosed");
        assertThat(serializedJson).extractingJsonPathStringValue("$.reservation.reservationId").isEqualTo("abc-123");
        assertThat(serializedJson).extractingJsonPathNumberValue("$.reservation.tickets[0].programId").isEqualTo(1);
        assertThat(serializedJson).extractingJsonPathNumberValue("$.reservation.tickets[0].sectorId").isEqualTo(2);
        assertThat(serializedJson).extractingJsonPathNumberValue("$.reservation.tickets[0].seatId").isEqualTo(3);

        assertThat(json.parse(serializedJson.getJson()))
                .isEqualTo(reservationClosed);
    }
}

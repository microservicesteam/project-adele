package com.microserviceteam.adele.ticketmaster.events;

import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.BOOKED;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.boot.test.json.JsonContent;

import com.microservicesteam.adele.ticketmaster.events.Event;
import com.microservicesteam.adele.ticketmaster.events.ReservationRejected;
import com.microservicesteam.adele.ticketmaster.model.Position;
import com.microservicesteam.adele.ticketmaster.model.Reservation;
import com.microservicesteam.adele.ticketmaster.model.Ticket;

public class ReservationRejectedSerializationTest extends AbstractSerializationTest {

    @Test
    public void serialize() throws Exception {
        ReservationRejected reservationRejected = ReservationRejected.builder()
                .reservation(Reservation.builder()
                        .reservationId("abc-123")
                        .addTickets(Ticket.builder()
                                .status(BOOKED)
                                .position(Position.builder()
                                        .eventId(1L)
                                        .sectorId(2)
                                        .seatId(3)
                                        .build())
                                .build())
                        .build())
                .build();

        JsonContent<Event> serializedJson = json.write(reservationRejected);
        assertThat(serializedJson).extractingJsonPathStringValue("type").isEqualTo("ReservationRejected");
        assertThat(serializedJson).extractingJsonPathStringValue("$.reservation.reservationId").isEqualTo("abc-123");
        assertThat(serializedJson).extractingJsonPathStringValue("$.reservation.tickets[0].status").isEqualTo("BOOKED");
        assertThat(serializedJson).extractingJsonPathNumberValue("$.reservation.tickets[0].position.eventId").isEqualTo(1);
        assertThat(serializedJson).extractingJsonPathNumberValue("$.reservation.tickets[0].position.sectorId").isEqualTo(2);
        assertThat(serializedJson).extractingJsonPathNumberValue("$.reservation.tickets[0].position.seatId").isEqualTo(3);

        assertThat(json.parse(serializedJson.getJson()))
                .isEqualTo(reservationRejected);
    }
}

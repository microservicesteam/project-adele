package com.microserviceteam.adele.ticketmaster.events;

import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.boot.test.json.JsonContent;

import com.microservicesteam.adele.ticketmaster.events.Event;
import com.microservicesteam.adele.ticketmaster.events.TicketsCreated;
import com.microservicesteam.adele.ticketmaster.model.Position;
import com.microservicesteam.adele.ticketmaster.model.Ticket;

public class TicketsCreatedSerializationTest extends AbstractSerializationTest {

    @Test
    public void serialize() throws Exception {
        TicketsCreated ticketsCreated = TicketsCreated.builder()
                .addTickets(Ticket.builder()
                        .status(FREE)
                        .position(Position.builder()
                                .eventId(1L)
                                .sectorId(2)
                                .seatId(3)
                                .build())
                        .build())
                .build();

        JsonContent<Event> serializedJson = json.write(ticketsCreated);
        assertThat(serializedJson).extractingJsonPathStringValue("type").isEqualTo("TicketsCreated");
        assertThat(serializedJson).extractingJsonPathStringValue("$.tickets[0].status").isEqualTo("FREE");
        assertThat(serializedJson).extractingJsonPathNumberValue("$.tickets[0].position.eventId").isEqualTo(1);
        assertThat(serializedJson).extractingJsonPathNumberValue("$.tickets[0].position.sectorId").isEqualTo(2);
        assertThat(serializedJson).extractingJsonPathNumberValue("$.tickets[0].position.seatId").isEqualTo(3);

        assertThat(json.parse(serializedJson.getJson()))
                .isEqualTo(ticketsCreated);
    }
}
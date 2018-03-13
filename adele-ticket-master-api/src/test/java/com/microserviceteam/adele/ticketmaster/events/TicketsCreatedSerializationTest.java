package com.microserviceteam.adele.ticketmaster.events;

import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.springframework.boot.test.json.JsonContent;

import com.microservicesteam.adele.ticketmaster.events.Event;
import com.microservicesteam.adele.ticketmaster.events.TicketsCreated;
import com.microservicesteam.adele.ticketmaster.model.Ticket;
import com.microservicesteam.adele.ticketmaster.model.TicketId;

public class TicketsCreatedSerializationTest extends AbstractSerializationTest {

    @Test
    public void serialize() throws IOException {
        TicketsCreated ticketsCreated = TicketsCreated.builder()
                .addTickets(Ticket.builder()
                        .status(FREE)
                        .ticketId(TicketId.builder()
                                .programId(1L)
                                .sectorId(2)
                                .seatId(3)
                                .build())
                        .build())
                .build();

        JsonContent<Event> serializedJson = json.write(ticketsCreated);
        assertThat(serializedJson).extractingJsonPathStringValue("type").isEqualTo("TicketsCreated");
        assertThat(serializedJson).extractingJsonPathStringValue("$.tickets[0].status").isEqualTo("FREE");
        assertThat(serializedJson).extractingJsonPathNumberValue("$.tickets[0].ticketId.programId").isEqualTo(1);
        assertThat(serializedJson).extractingJsonPathNumberValue("$.tickets[0].ticketId.sectorId").isEqualTo(2);
        assertThat(serializedJson).extractingJsonPathNumberValue("$.tickets[0].ticketId.seatId").isEqualTo(3);

        assertThat(json.parse(serializedJson.getJson()))
                .isEqualTo(ticketsCreated);
    }
}
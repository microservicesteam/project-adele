package com.microserviceteam.adele.ticketmaster.events;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.boot.test.json.JsonContent;

import com.microservicesteam.adele.ticketmaster.events.TicketsCancelled;
import com.microservicesteam.adele.ticketmaster.events.TicketsCreated;
import com.microservicesteam.adele.ticketmaster.events.TicketsEvent;
import com.microservicesteam.adele.ticketmaster.model.Position;

public class TicketsCancelledSerializationTest extends AbstractSerializationTest {

    @Test
    public void serialize() throws Exception {
        TicketsCancelled ticketsCancelled = TicketsCancelled.builder()
                .bookingId("abc-123")
                .addPositions(Position.builder()
                        .eventId(1L)
                        .sectorId(1)
                        .id(1)
                        .build())
                .build();

        JsonContent<TicketsEvent> serializedJson = json.write(ticketsCancelled);
        assertThat(serializedJson).extractingJsonPathStringValue("type").isEqualTo("TicketsCancelled");
        assertThat(serializedJson).extractingJsonPathStringValue("bookingId").isEqualTo("abc-123");
        assertThat(serializedJson).extractingJsonPathNumberValue("$.positions[0].eventId").isEqualTo(1);
        assertThat(serializedJson).extractingJsonPathNumberValue("$.positions[0].sectorId").isEqualTo(1);
        assertThat(serializedJson).extractingJsonPathNumberValue("$.positions[0].id").isEqualTo(1);

        assertThat(json.parse(serializedJson.getJson()))
                .isEqualTo(ticketsCancelled);
    }
}
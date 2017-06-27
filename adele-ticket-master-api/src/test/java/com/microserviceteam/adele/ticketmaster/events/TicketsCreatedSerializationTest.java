package com.microserviceteam.adele.ticketmaster.events;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.boot.test.json.JsonContent;

import com.microservicesteam.adele.ticketmaster.events.TicketsCreated;
import com.microservicesteam.adele.ticketmaster.events.TicketsEvent;
import com.microservicesteam.adele.ticketmaster.model.Position;

public class TicketsCreatedSerializationTest extends AbstractSerializationTest {

    @Test
    public void serialize() throws Exception {
        TicketsCreated ticketsCreated = TicketsCreated.builder()
                .addPositions(Position.builder()
                        .eventId(1L)
                        .sectorId(1)
                        .id(1)
                        .build())
                .build();

        JsonContent<TicketsEvent> serializedJson = json.write(ticketsCreated);
        assertThat(serializedJson).extractingJsonPathStringValue("type").isEqualTo("TicketsCreated");
        assertThat(serializedJson).extractingJsonPathNumberValue("$.positions[0].eventId").isEqualTo(1);
        assertThat(serializedJson).extractingJsonPathNumberValue("$.positions[0].sectorId").isEqualTo(1);
        assertThat(serializedJson).extractingJsonPathNumberValue("$.positions[0].id").isEqualTo(1);

        assertThat(json.parse(serializedJson.getJson()))
                .isEqualTo(ticketsCreated);
    }
}
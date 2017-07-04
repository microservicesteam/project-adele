package com.microservicesteam.adele.ticketmaster.events;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.microservicesteam.adele.messaging.events.Event;
import com.microservicesteam.adele.ticketmaster.model.Position;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
        include=JsonTypeInfo.As.PROPERTY,
        property="type")
@JsonSubTypes({
        @JsonSubTypes.Type(value=TicketsCreated.class, name="TicketsCreated"),
        @JsonSubTypes.Type(value=TicketsBooked.class, name="TicketsBooked"),
        @JsonSubTypes.Type(value=TicketsCancelled.class, name="TicketsCancelled"),
        @JsonSubTypes.Type(value=TicketsPaid.class, name="TicketsPaid"),
})
public interface TicketsEvent extends Event {
    List<Position> positions();
}

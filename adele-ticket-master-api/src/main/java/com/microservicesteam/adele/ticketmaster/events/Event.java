package com.microservicesteam.adele.ticketmaster.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
        include=JsonTypeInfo.As.PROPERTY,
        property="type")
@JsonSubTypes({
        @JsonSubTypes.Type(value=TicketsCreated.class, name="TicketsCreated"),
        @JsonSubTypes.Type(value=ReservationAccepted.class, name="ReservationAccepted"),
        @JsonSubTypes.Type(value=ReservationCancelled.class, name="ReservationCancelled"),
        @JsonSubTypes.Type(value=ReservationRejected.class, name="ReservationRejected")
})
public interface Event {
}

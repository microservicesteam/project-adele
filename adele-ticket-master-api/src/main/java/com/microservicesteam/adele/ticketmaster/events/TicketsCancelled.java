package com.microservicesteam.adele.ticketmaster.events;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableTicketsCancelled.class)
@JsonDeserialize(as = ImmutableTicketsCancelled.class)
@JsonTypeName("TicketsCancelled")
public interface TicketsCancelled extends TicketsEvent {
    String bookingId();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableTicketsCancelled.Builder {
    }
}

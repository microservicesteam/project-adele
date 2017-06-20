package com.microservicesteam.adele.ticketmaster.events;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableTicketsBooked.class)
@JsonDeserialize(as = ImmutableTicketsBooked.class)
@JsonTypeName("TicketsBooked")
public interface TicketsBooked extends TicketsEvent {
    String bookingId();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableTicketsBooked.Builder {
    }
}

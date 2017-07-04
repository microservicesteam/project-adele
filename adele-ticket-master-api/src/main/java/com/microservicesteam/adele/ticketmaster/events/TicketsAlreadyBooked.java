package com.microservicesteam.adele.ticketmaster.events;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableTicketsAlreadyBooked.class)
@JsonDeserialize(as = ImmutableTicketsAlreadyBooked.class)
@JsonTypeName("TicketsAlreadyBooked")
public interface TicketsAlreadyBooked extends TicketsEvent {

    String bookingId();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableTicketsAlreadyBooked.Builder {
    }

}

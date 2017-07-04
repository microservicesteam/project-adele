package com.microservicesteam.adele.ticketmaster.events;

import org.immutables.value.Value;

@Value.Immutable
public interface TicketsWereAlreadyBooked extends TicketsEvent {

    String bookingId();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableTicketsWereAlreadyBooked.Builder {
    }

}

package com.microservicesteam.adele.ticketmaster.events;

import org.immutables.value.Value;

@Value.Immutable
public interface TicketsCancelled extends TicketsEvent {
    long bookingId();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableTicketsCancelled.Builder {
    }
}

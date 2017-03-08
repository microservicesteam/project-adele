package com.microservicesteam.adele.ticketmaster.events;

import org.immutables.value.Value;

@Value.Immutable
public interface TicketsCreatedEvent extends TicketsEvent {

    static Builder builder(){
        return new Builder();
    }
    class Builder extends ImmutableTicketsCreatedEvent.Builder {
    }
}

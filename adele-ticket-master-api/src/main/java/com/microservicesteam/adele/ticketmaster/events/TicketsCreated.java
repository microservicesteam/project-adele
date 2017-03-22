package com.microservicesteam.adele.ticketmaster.events;

import org.immutables.value.Value;

@Value.Immutable
public interface TicketsCreated extends TicketsEvent {

    static Builder builder(){
        return new Builder();
    }
    class Builder extends ImmutableTicketsCreated.Builder {
    }
}

package com.microservicesteam.adele.ticketmaster.events;

import org.immutables.value.Value;

@Value.Immutable
public interface TicketsClaimedEvent extends TicketsEvent {

    static Builder builder(){
        return new Builder();
    }
    class Builder extends ImmutableTicketsClaimedEvent.Builder {
    }

}

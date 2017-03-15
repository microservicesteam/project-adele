package com.microservicesteam.adele.ticketmaster.model;

import org.immutables.value.Value;

@Value.Immutable
public interface FreeTicket extends Ticket {

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableFreeTicket.Builder {
    }

}

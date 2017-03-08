package com.microservicesteam.adele.ticketmaster.model;

import org.immutables.value.Value;

@Value.Immutable
public interface TicketId {
    int sector();
    int position();

    class Builder extends ImmutableTicketId.Builder {
    }

    static Builder builder() {
        return new Builder();
    }
}

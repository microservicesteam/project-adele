package com.microservicesteam.adele.ticketmaster.model;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableTicket.class)
@JsonDeserialize(as = ImmutableTicket.class)
public interface Ticket {
    TicketId ticketId();

    TicketStatus status();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableTicket.Builder {
    }
}

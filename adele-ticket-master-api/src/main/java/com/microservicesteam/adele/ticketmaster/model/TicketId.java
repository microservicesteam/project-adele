package com.microservicesteam.adele.ticketmaster.model;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableTicketId.class)
@JsonDeserialize(as = ImmutableTicketId.class)
public interface TicketId {
    long programId();

    int sectorId();

    int seatId();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableTicketId.Builder {
    }
}

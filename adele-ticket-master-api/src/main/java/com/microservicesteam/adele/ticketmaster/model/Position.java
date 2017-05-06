package com.microservicesteam.adele.ticketmaster.model;

import org.immutables.value.Value;

@Value.Immutable
public interface Position {
    long eventId();
    int sectorId();
    int id();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutablePosition.Builder {
    }
}

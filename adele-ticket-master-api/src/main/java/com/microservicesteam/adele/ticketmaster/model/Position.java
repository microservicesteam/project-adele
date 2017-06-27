package com.microservicesteam.adele.ticketmaster.model;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutablePosition.class)
@JsonDeserialize(as = ImmutablePosition.class)
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

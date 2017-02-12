package com.microservicesteam.adele.model;

import java.util.List;

import org.immutables.value.Value;

@Value.Immutable
public interface Venue {

    Long id();

    String address();

    Coordinates coordinates();

    List<Sector> sectors();

    class Builder extends ImmutableVenue.Builder {
    }

    static Builder builder() {
        return new Builder();
    }

}

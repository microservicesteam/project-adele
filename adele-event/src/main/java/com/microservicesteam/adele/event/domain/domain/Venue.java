package com.microservicesteam.adele.event.domain.domain;

import java.util.List;

import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
public interface Venue {

    @Nullable
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

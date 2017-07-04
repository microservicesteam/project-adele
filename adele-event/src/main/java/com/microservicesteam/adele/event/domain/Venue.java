package com.microservicesteam.adele.event.domain;

import javax.annotation.Nullable;
import java.util.List;

import org.immutables.value.Value;

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

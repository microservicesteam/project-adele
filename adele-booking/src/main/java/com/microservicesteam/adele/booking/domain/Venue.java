package com.microservicesteam.adele.booking.domain;

import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.util.List;

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

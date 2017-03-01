package com.microservicesteam.adele.model;

import javax.annotation.Nullable;

import org.immutables.value.Value;

@Value.Immutable
public interface Coordinates {

    @Nullable
    Long id();
    
    double latitude();

    double longitude();

    class Builder extends ImmutableCoordinates.Builder {
    }

    static Builder builder() {
        return new Builder();
    }

}

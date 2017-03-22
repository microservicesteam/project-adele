package com.microservicesteam.adele.model;

import org.immutables.value.Value;

@Value.Immutable
public interface Coordinates {
    
    double latitude();

    double longitude();

    class Builder extends ImmutableCoordinates.Builder {
    }

    static Builder builder() {
        return new Builder();
    }

}

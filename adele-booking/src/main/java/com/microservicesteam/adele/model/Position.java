package com.microservicesteam.adele.model;

import org.immutables.value.Value;

@Value.Immutable
public interface Position {

    Long id();
    
    Sector sector();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutablePosition.Builder {
    }

}

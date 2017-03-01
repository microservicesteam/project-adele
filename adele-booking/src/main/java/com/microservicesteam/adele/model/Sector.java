package com.microservicesteam.adele.model;

import org.immutables.value.Value;

@Value.Immutable
public interface Sector {
	
	Long id();

    long capacity();

    Price price();

    Position position();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableSector.Builder {
    }
}

package com.microservicesteam.adele.booking.domain;

import org.immutables.value.Value;

@Value.Immutable
public interface User {

    Long id();
    
    class Builder extends ImmutableUser.Builder {
    }

    static Builder builder() {
        return new Builder();
    }

    
}

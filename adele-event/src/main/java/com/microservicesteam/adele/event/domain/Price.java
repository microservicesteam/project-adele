package com.microservicesteam.adele.event.domain;

import java.math.BigDecimal;

import org.immutables.value.Value;

@Value.Immutable
public interface Price {
    
    BigDecimal amount();
    
    String currency();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutablePrice.Builder {
    }
}

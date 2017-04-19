package com.microservicesteam.adele.booking.domain;

import org.immutables.value.Value;

import java.math.BigDecimal;

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

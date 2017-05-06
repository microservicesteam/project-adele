package com.microservicesteam.adele.booking.domain;

import org.immutables.value.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value.Immutable
public interface Visitor {
    
    Long id();
    
    String name();
    
    LocalDate birthDate();
    
    String address();
    
    BigDecimal discountInPercent();
    
    class Builder extends ImmutableVisitor.Builder {
    }

    static Builder builder() {
        return new Builder();
    }

}

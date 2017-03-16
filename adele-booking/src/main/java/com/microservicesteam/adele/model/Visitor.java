package com.microservicesteam.adele.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.immutables.value.Value;

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

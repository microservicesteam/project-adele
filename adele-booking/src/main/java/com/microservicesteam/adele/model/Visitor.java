package com.microservicesteam.adele.model;

import java.time.LocalDate;

import org.immutables.value.Value;

@Value.Immutable
public interface Visitor {
    
    Long id();
    
    String name();
    
    LocalDate birthDate();
    
    String address();
    
    int discountInPercent();
    
    class Builder extends ImmutableVisitor.Builder {
    }

    static Builder builder() {
        return new Builder();
    }

}

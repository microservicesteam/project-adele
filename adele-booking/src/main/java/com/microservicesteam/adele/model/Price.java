package com.microservicesteam.adele.model;

import java.math.BigDecimal;

import org.immutables.value.Value;

@Value.Immutable
public interface Price {
    
    BigDecimal amount();
    
    String currency();

}

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
    

}

package com.microservicesteam.adele.model;

import org.immutables.value.Value;
import org.joda.time.LocalDate;

@Value.Immutable
public interface Visitor {
    
    Long id();
    
    String name();
    
    LocalDate birthDate();
    
    String address();
    
    int discountInPercent();
    

}

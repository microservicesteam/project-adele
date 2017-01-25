package com.microservicesteam.adele.model;

import java.util.Optional;

import org.immutables.value.Value;

@Value.Immutable
public interface Ticket {

    Event event();
    
    Sector sector();
    
    Optional<Position> position();
    
    Visitor visitor();
    
    Price price();
    
    
    
}

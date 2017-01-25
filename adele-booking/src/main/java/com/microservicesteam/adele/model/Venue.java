package com.microservicesteam.adele.model;

import java.util.List;

import org.immutables.value.Value;

@Value.Immutable
public interface Venue {

    String address();
    
    Coordinates coordinates();
    
    List<Sector> sectors();
    
}

package com.microservicesteam.adele.model;

import java.util.Map;
import java.util.Optional;

import org.immutables.value.Value;

@Value.Immutable
public interface Sector {
    
    long capacity();
    
    Price price();
    
    Position position();
    
    Optional<Map<Position, SeatStatus>> map();

}

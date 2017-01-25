package com.microservicesteam.adele.model;

import org.immutables.value.Value;
import org.joda.time.DateTime;

@Value.Immutable
public interface Event {

    String name();
    
    String description();
    
    Venue venue();
    
    DateTime dateTime();
    
    EventStatus status();
    
}

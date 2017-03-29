package com.microservicesteam.adele.model;

import org.immutables.value.Value;

@Value.Immutable
public interface Ticket {

    Long id();
	
    Event event();
    
    Integer position();
    
    Visitor visitor();
    
    Price price();
    
    class Builder extends ImmutableTicket.Builder {
    }

    static Builder builder() {
        return new Builder();
    }

    
}

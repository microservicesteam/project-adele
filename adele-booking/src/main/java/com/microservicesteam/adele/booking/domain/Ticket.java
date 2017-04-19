package com.microservicesteam.adele.booking.domain;

import org.immutables.value.Value;

@Value.Immutable
public interface Ticket {

    Long id();
	
    Long eventId();
    
    Integer position();
    
    Visitor visitor();
    
    Price price();
    
    class Builder extends ImmutableTicket.Builder {
    }

    static Builder builder() {
        return new Builder();
    }

    
}

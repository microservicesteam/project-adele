package com.microservicesteam.adele.model;

import java.util.List;

import org.immutables.value.Value;

@Value.Immutable
public interface Booking {

    Long id();
	
    User user();
    
    List<Ticket> tickets();
    
    Price price();
    
    String paymentId();
    
    PaymentStatus status();
    
    class Builder extends ImmutableBooking.Builder {
    }

    static Builder builder() {
        return new Builder();
    }
    
}

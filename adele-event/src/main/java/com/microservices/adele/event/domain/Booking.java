package com.microservices.adele.event.domain;

import java.util.List;

import org.immutables.value.Value;

@Value.Immutable
public interface Booking {

    Long id();
	
    User user();
    
    List<Ticket> tickets();
    
    Price sumPrice();
    
    String paymentId();
    
    PaymentStatus status();
    
    class Builder extends ImmutableBooking.Builder {
    }

    static Builder builder() {
        return new Builder();
    }
    
}

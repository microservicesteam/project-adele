package com.microservicesteam.adele.booking.domain;

import org.immutables.value.Value;

import java.util.List;

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
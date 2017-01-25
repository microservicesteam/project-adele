package com.microservicesteam.adele.model;

import java.util.List;

import org.immutables.value.Value;

@Value.Immutable
public interface Booking {

    User user();
    
    List<Ticket> tickets();
    
    Price price();
    
    String paymentId();
    
    PaymentStatus status();
    
}

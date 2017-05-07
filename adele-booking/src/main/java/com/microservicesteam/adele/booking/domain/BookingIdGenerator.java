package com.microservicesteam.adele.booking.domain;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class BookingIdGenerator {
    String generateBookingId(){
        return UUID.randomUUID().toString();
    }
}

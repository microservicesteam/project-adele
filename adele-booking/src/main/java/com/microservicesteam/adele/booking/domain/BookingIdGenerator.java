package com.microservicesteam.adele.booking.domain;

import java.util.UUID;

public class BookingIdGenerator {
    String generateBookingId(){
        return UUID.randomUUID().toString();
    }
}

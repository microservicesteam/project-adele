package com.microservicesteam.adele.ticketmaster;

import java.util.UUID;

public class BookingIdGenerator {
    String generateBookingId(){
        return UUID.randomUUID().toString();
    }
}

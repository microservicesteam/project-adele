package com.microservicesteam.adele.booking.domain;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class ReservationIdGenerator {
    String generateReservationId(){
        return UUID.randomUUID().toString();
    }
}

package com.microservicesteam.adele.ticketmaster.model;

import org.immutables.value.Value;

import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;

public interface Ticket {
    Position position();

    @Value.Default
    default TicketStatus status() {
        return FREE;
    }
}

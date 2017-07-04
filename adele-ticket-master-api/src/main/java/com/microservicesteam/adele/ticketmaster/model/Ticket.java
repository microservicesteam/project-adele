package com.microservicesteam.adele.ticketmaster.model;

import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;

import org.immutables.value.Value;

public interface Ticket {
    Position position();

    @Value.Default
    default TicketStatus status() {
        return FREE;
    }
}

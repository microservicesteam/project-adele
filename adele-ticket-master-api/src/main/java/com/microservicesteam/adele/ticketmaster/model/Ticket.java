package com.microservicesteam.adele.ticketmaster.model;

import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;

public interface Ticket {
    long eventId();
    Position position();

    default TicketStatus status() {
        return FREE;
    }
}

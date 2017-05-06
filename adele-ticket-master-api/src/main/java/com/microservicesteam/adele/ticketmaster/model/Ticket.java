package com.microservicesteam.adele.ticketmaster.model;

import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;

public interface Ticket {
    Position position();

    default TicketStatus status() {
        return FREE;
    }
}

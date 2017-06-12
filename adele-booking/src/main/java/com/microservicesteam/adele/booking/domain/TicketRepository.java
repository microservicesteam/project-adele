package com.microservicesteam.adele.booking.domain;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.microservicesteam.adele.ticketmaster.model.*;
import com.microservicesteam.adele.ticketmaster.model.Ticket;

public class TicketRepository {

    private final Map<Position, Ticket> tickets;

    public TicketRepository() {
        this.tickets = new HashMap<>();
    }

    public void put(Position position, Ticket ticket) {
        tickets.put(position, ticket);
    }

    public ImmutableList<Ticket> getTicketsStatus() {
        return ImmutableList.copyOf(tickets.values());
    }
}

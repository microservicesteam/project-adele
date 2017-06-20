package com.microservicesteam.adele.booking.domain;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.microservicesteam.adele.ticketmaster.model.Position;
import com.microservicesteam.adele.ticketmaster.model.Ticket;

@Service
public class TicketRepository {

    private final Map<Position, Ticket> tickets;

    public TicketRepository() {
        this.tickets = new HashMap<>();
    }

    public void put(Ticket ticket) {
        tickets.put(ticket.position(), ticket);
    }

    public boolean has(Position position) {
        return tickets.containsKey(position);
    }

    public Ticket get(Position position) {
        return tickets.get(position);
    }

    ImmutableList<Ticket> getTicketsStatus() {
        return tickets.values().stream()
                .sorted(comparingInt(t -> t.position().id()))
                .collect(collectingAndThen(toList(), ImmutableList::copyOf));
    }
}

package com.microservicesteam.adele.booking.domain;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.Comparator.comparingInt;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    ImmutableList<Ticket> getTicketsStatusByEvent(long eventId, Optional<Integer> sector) {
        return tickets.values().stream()
                .filter(t -> t.position().eventId() == eventId && (!sector.isPresent() || t.position().sectorId() == sector.get()))
                .sorted(comparingInt(t -> t.position().id()))
                .collect(toImmutableList());
    }
}

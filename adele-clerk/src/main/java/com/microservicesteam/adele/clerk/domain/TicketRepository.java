package com.microservicesteam.adele.clerk.domain;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.Comparator.comparingInt;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

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

    public ImmutableList<Ticket> getTicketsStatusByEvent(long eventId) {
        return filterTicketsBy(matchingEventId(eventId));
    }

    public ImmutableList<Ticket> getTicketsStatusByEventAndSector(long eventId, int sector) {
        return filterTicketsBy(matchingEventIdAndSector(eventId, sector));
    }

    private ImmutableList<Ticket> filterTicketsBy(Predicate<Ticket> predicate) {
        return tickets.values().stream()
                .filter(predicate)
                .sorted(comparingInt(t -> t.position().seatId()))
                .collect(toImmutableList());
    }

    private Predicate<Ticket> matchingEventId(long eventId) {
        return ticket -> ticket.position().eventId() == eventId;
    }

    private Predicate<Ticket> matchingEventIdAndSector(long eventId, int sector) {
        return matchingEventId(eventId)
                .and(ticket -> ticket.position().sectorId() == sector);
    }
}

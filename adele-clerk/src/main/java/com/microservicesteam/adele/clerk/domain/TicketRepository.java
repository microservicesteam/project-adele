package com.microservicesteam.adele.clerk.domain;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.Comparator.comparingInt;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.microservicesteam.adele.ticketmaster.model.Ticket;
import com.microservicesteam.adele.ticketmaster.model.TicketId;

@Service
public class TicketRepository {

    private final Map<TicketId, Ticket> tickets;

    public TicketRepository() {
        this.tickets = new HashMap<>();
    }

    public void put(Ticket ticket) {
        tickets.put(ticket.ticketId(), ticket);
    }

    public boolean has(TicketId ticketId) {
        return tickets.containsKey(ticketId);
    }

    public Ticket get(TicketId ticketId) {
        return tickets.get(ticketId);
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
                .sorted(comparingInt(t -> t.ticketId().seatId()))
                .collect(toImmutableList());
    }

    private Predicate<Ticket> matchingEventId(long eventId) {
        return ticket -> ticket.ticketId().eventId() == eventId;
    }

    private Predicate<Ticket> matchingEventIdAndSector(long eventId, int sector) {
        return matchingEventId(eventId)
                .and(ticket -> ticket.ticketId().sectorId() == sector);
    }
}

package com.microservicesteam.adele.ticketmaster.commands;

import java.util.List;

import com.microservicesteam.adele.ticketmaster.model.Position;

public interface TicketsCommand {
    long eventId();
    List<Position> positions();
}

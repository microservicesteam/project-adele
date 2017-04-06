package com.microservicesteam.adele.ticketmaster.events;

import java.util.List;

import com.microservicesteam.adele.messaging.events.Event;
import com.microservicesteam.adele.ticketmaster.model.Position;

interface TicketsEvent extends Event {
    List<Position> positions();
}

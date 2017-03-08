package com.microservicesteam.adele.ticketmaster.events;

import java.util.List;

import com.microservicesteam.adele.messaging.events.Event;

interface TicketsEvent extends Event {
    int sectorId();
    List<Integer> positions();
}

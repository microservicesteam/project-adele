package com.microservicesteam.adele.booking.boundary.web;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableSet;
import com.microservicesteam.adele.ticketmaster.events.TicketsEvent;
import com.microservicesteam.adele.ticketmaster.model.Position;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WebSocketEventPublisher {

    private SimpMessagingTemplate template;

    public WebSocketEventPublisher(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void publish(TicketsEvent event) {
        this.template.convertAndSend("/topic/tickets", event);
        log.debug("Event was published: {}", event);
    }

    public void publishToSector(TicketsEvent event) {
        ImmutableSet<Integer> sectors = extractSectors(event);
        sectors.forEach(sector -> publishToSector(sector, event));
    }

    private ImmutableSet<Integer> extractSectors(TicketsEvent event) {
        return event.positions().stream()
                .map(Position::sectorId)
                .collect(toImmutableSet());
    }

    private void publishToSector(Integer sector, TicketsEvent event) {
        this.template.convertAndSend("/topic/tickets/sector/" + sector, event);
        log.debug("Event was published: {}", event);
    }
}

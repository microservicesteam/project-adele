package com.microservicesteam.adele.booking.boundary.web;

import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

@Component
@Slf4j
public class WebSocketEventPublisher {

    private SimpMessagingTemplate template;

    public WebSocketEventPublisher(SimpMessagingTemplate template) {
        this.template = template;
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
        this.template.convertAndSend("/topic/sectors/" + sector + "/tickets", event);
        log.debug("Event was published: {}", event);
    }
}

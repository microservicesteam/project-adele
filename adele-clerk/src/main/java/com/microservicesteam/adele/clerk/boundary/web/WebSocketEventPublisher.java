package com.microservicesteam.adele.clerk.boundary.web;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableSet;
import com.microservicesteam.adele.ticketmaster.events.ReservationEvent;
import com.microservicesteam.adele.ticketmaster.model.TicketId;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@AllArgsConstructor
public class WebSocketEventPublisher {

    private SimpMessagingTemplate template;

    public void publishToSector(ReservationEvent event) {
        ImmutableSet<Integer> sectors = extractSectors(event);
        sectors.forEach(sector -> publishToSector(sector, event));
    }

    private ImmutableSet<Integer> extractSectors(ReservationEvent event) {
        return event.reservation().tickets().stream()
                .map(TicketId::sectorId)
                .collect(toImmutableSet());
    }

    private void publishToSector(Integer sector, ReservationEvent event) {
        this.template.convertAndSend("/topic/sectors/" + sector + "/reservations", event);
        log.debug("Event was published: {}", event);
    }
}

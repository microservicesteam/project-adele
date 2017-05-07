package com.microservicesteam.adele.booking.boundary.web;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.microservicesteam.adele.messaging.events.Event;

@Component
public class WebSocketEventPublisher {

    private SimpMessagingTemplate template;

    public WebSocketEventPublisher(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void publish(Event event) {
        this.template.convertAndSend("/topic/tickets", event);
    }
}

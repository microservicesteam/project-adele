package com.microservicesteam.adele.booking.boundary.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.microservicesteam.adele.messaging.events.Event;

@Component
public class WebSocketEventPublisher {

    private static Logger LOGGER = LoggerFactory.getLogger(WebSocketEventPublisher.class);

    private SimpMessagingTemplate template;

    public WebSocketEventPublisher(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void publish(Event event) {
        LOGGER.debug("Event was published: {}", event);
        this.template.convertAndSend("/topic/tickets", event);
    }
}

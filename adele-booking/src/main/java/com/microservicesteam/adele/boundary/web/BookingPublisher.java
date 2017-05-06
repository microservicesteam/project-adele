package com.microservicesteam.adele.boundary.web;

import com.microservicesteam.adele.messaging.events.Event;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookingPublisher {

    private SimpMessagingTemplate template;

    public BookingPublisher(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void publish(Event event) {
        this.template.convertAndSend("/topic/tickets", event);
    }
}

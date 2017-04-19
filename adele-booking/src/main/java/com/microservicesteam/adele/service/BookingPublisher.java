package com.microservicesteam.adele.service;

import com.microservicesteam.adele.messaging.events.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookingPublisher {

    private SimpMessagingTemplate template;

    @Autowired
    public BookingPublisher(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void publish(Event event) {
        this.template.convertAndSend("/topic/tickets", event);
    }
}

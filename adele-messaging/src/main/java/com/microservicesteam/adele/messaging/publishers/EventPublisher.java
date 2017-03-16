package com.microservicesteam.adele.messaging.publishers;

import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.microservicesteam.adele.messaging.events.Event;

@Component
public class EventPublisher {

    private final EventBus eventBus;

    public EventPublisher(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void publish(Event event) {
        eventBus.post(event);
    }
}

package com.microservicesteam.adele.booking.boundary.web;

import com.microservicesteam.adele.messaging.events.Event;

public interface EventPublisher {

    void publish(Event event);
}

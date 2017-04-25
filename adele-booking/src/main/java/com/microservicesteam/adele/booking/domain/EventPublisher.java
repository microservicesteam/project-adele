package com.microservicesteam.adele.booking.domain;

import com.microservicesteam.adele.messaging.events.Event;

public interface EventPublisher {

    void publish(Event event);
}

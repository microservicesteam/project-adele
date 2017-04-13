package com.microservicesteam.adele.service;

import com.microservicesteam.adele.model.Event;

public interface EventPublisher {

    void publish(Event event);
}

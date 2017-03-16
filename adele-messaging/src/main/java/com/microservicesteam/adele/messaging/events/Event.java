package com.microservicesteam.adele.messaging.events;

import java.time.LocalDateTime;

public interface Event {

    Long id();

    LocalDateTime timestamp();

    EventType eventType();
}

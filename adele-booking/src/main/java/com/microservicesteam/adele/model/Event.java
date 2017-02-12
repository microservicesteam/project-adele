package com.microservicesteam.adele.model;

import java.time.LocalDateTime;

import org.immutables.value.Value;

@Value.Immutable
public interface Event {

    Long id();

    String name();

    String description();

    Venue venue();

    LocalDateTime dateTime();

    EventStatus status();

    class Builder extends ImmutableEvent.Builder {
    }

    static Builder builder() {
        return new Builder();
    }
}

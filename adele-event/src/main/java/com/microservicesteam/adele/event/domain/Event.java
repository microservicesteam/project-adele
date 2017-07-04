package com.microservicesteam.adele.event.domain;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

import org.immutables.value.Value;

@Value.Immutable
public interface Event {

    @Nullable
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

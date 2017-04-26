package com.microservicesteam.adele.event.domain.domain;

import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

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

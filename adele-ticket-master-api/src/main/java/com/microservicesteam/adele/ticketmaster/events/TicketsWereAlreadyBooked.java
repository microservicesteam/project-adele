package com.microservicesteam.adele.ticketmaster.events;

import com.microservicesteam.adele.messaging.events.Event;
import org.immutables.value.Value;

@Value.Immutable
public interface TicketsWereAlreadyBooked extends Event {

    String bookingId();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableTicketsWereAlreadyBooked.Builder {
    }

}

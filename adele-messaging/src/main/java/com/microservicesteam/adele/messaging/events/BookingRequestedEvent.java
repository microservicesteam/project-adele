package com.microservicesteam.adele.messaging.events;

import static com.microservicesteam.adele.messaging.events.EventType.BOOKING_REQUESTED;

import org.immutables.value.Value;

@Value.Immutable
public interface BookingRequestedEvent extends Event {

    @Value.Derived
    default EventType eventType() {
        return BOOKING_REQUESTED;
    }


}

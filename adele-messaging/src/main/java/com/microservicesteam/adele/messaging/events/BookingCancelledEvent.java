package com.microservicesteam.adele.messaging.events;

import static com.microservicesteam.adele.messaging.events.EventType.BOOKING_CANCELLED;

import org.immutables.value.Value;

import com.microservicesteam.adele.messaging.events.ImmutableBookingCancelledEvent.Builder;

@Value.Immutable
public interface BookingCancelledEvent extends Event {

    @Value.Derived
    default EventType eventType() {
        return BOOKING_CANCELLED;
    }

    static Builder builder(){
        return ImmutableBookingCancelledEvent.builder();
    }
}

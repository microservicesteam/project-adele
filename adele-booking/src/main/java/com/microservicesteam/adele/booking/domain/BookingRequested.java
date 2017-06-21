package com.microservicesteam.adele.booking.domain;

import org.immutables.value.Value;

@Value.Immutable
public interface BookingRequested extends BookingResponse {

    String bookingId();

    class Builder extends ImmutableBookingRequested.Builder {
    }

    static Builder builder() {
        return new Builder();
    }

}

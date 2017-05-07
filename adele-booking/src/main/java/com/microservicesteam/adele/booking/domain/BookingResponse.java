package com.microservicesteam.adele.booking.domain;

import org.immutables.value.Value;

@Value.Immutable
public interface BookingResponse {
    String bookingId();

    class Builder extends ImmutableBookingResponse.Builder {
    }

    static Builder builder() {
        return new Builder();
    }

}

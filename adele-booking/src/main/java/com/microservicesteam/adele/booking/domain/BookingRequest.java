package com.microservicesteam.adele.booking.domain;

import java.util.List;

import org.immutables.value.Value;

@Value.Immutable
public interface BookingRequest {

    long eventId();

    int sectorId();

    List<Integer> positions();

    class Builder extends ImmutableBookingRequest.Builder {
    }

    static Builder builder() {
        return new Builder();
    }

}

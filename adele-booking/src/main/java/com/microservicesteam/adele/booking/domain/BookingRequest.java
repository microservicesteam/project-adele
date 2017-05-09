package com.microservicesteam.adele.booking.domain;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableBookingRequest.class)
@JsonDeserialize(as = ImmutableBookingRequest.class)
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

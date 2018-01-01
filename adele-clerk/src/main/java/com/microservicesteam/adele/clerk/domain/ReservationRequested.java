package com.microservicesteam.adele.clerk.domain;

import org.immutables.value.Value;

@Value.Immutable
public interface ReservationRequested extends ReservationResponse {

    String reservationId();

    class Builder extends ImmutableReservationRequested.Builder {
    }

    static Builder builder() {
        return new Builder();
    }
}

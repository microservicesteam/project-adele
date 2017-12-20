package com.microservicesteam.adele.ticketmaster.events;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableReservationAccepted.class)
@JsonDeserialize(as = ImmutableReservationAccepted.class)
@JsonTypeName("ReservationAccepted")
public interface ReservationAccepted extends ReservationEvent {

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableReservationAccepted.Builder {
    }
}

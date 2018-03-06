package com.microservicesteam.adele.ticketmaster.events;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableReservationRejected.class)
@JsonDeserialize(as = ImmutableReservationRejected.class)
@JsonTypeName("ReservationRejected")
public interface ReservationRejected extends ReservationEvent {

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableReservationRejected.Builder {
    }

}

package com.microservicesteam.adele.ticketmaster.events;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableReservationClosed.class)
@JsonDeserialize(as = ImmutableReservationClosed.class)
@JsonTypeName("ReservationClosed")
public interface ReservationClosed extends ReservationEvent {

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableReservationClosed.Builder {
    }

}

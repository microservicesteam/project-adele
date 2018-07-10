package com.microservicesteam.adele.ticketmaster.model;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableReservation.class)
@JsonDeserialize(as = ImmutableReservation.class)
public interface Reservation {

    String reservationId();

    List<TicketId> tickets();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableReservation.Builder {
    }

}

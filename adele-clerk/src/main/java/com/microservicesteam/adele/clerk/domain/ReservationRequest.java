package com.microservicesteam.adele.clerk.domain;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.microservicesteam.adele.ticketmaster.model.Position;

@Value.Immutable
@JsonSerialize(as = ImmutableReservationRequest.class)
@JsonDeserialize(as = ImmutableReservationRequest.class)
public interface ReservationRequest {

    List<Position> positions();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableReservationRequest.Builder {
    }

}

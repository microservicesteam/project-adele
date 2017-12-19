package com.microservicesteam.adele.booking.domain;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.microservicesteam.adele.ticketmaster.model.Ticket;

@Value.Immutable
@JsonSerialize(as = ImmutableBookingRequest.class)
@JsonDeserialize(as = ImmutableBookingRequest.class)
public interface ReservationRequest {

    List<Ticket> tickets();

//    default ImmutableList<Position> requestedPositions() {
//        return positions().stream()
//                .map(positionId -> Position.builder()
//                        .eventId(eventId())
//                        .sectorId(sectorId())
//                        .id(positionId)
//                        .build())
//                .collect(toImmutableList());
//    }

    class Builder extends ImmutableBookingRequest.Builder {
    }

    static Builder builder() {
        return new Builder();
    }

}

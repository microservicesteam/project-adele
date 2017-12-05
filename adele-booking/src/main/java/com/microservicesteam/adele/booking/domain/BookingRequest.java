package com.microservicesteam.adele.booking.domain;

import static com.google.common.collect.ImmutableList.toImmutableList;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableList;
import com.microservicesteam.adele.ticketmaster.model.Position;

//TODO rename to ReservationRequest
@Value.Immutable
@JsonSerialize(as = ImmutableBookingRequest.class)
@JsonDeserialize(as = ImmutableBookingRequest.class)
public interface BookingRequest {

    //TODO contains only list of Tickets

    long eventId();

    int sectorId();

    List<Integer> positions();

    default ImmutableList<Position> requestedPositions() {
        return positions().stream()
                .map(positionId -> Position.builder()
                        .eventId(eventId())
                        .sectorId(sectorId())
                        .id(positionId)
                        .build())
                .collect(toImmutableList());
    }

    class Builder extends ImmutableBookingRequest.Builder {
    }

    static Builder builder() {
        return new Builder();
    }

}

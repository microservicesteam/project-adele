package com.microservicesteam.adele.booking.domain;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import com.google.common.collect.ImmutableList;
import com.microservicesteam.adele.ticketmaster.model.Position;

@Value.Immutable
@JsonSerialize(as = ImmutableBookingRequest.class)
@JsonDeserialize(as = ImmutableBookingRequest.class)
public interface BookingRequest {

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
                .collect(collectingAndThen(toList(), ImmutableList::copyOf));
    }

    class Builder extends ImmutableBookingRequest.Builder {
    }

    static Builder builder() {
        return new Builder();
    }

}

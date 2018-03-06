package com.microservicesteam.adele.ticketmaster.commands;

import org.immutables.value.Value;

import com.microservicesteam.adele.ticketmaster.model.Reservation;

@Value.Immutable
public interface CreateReservation extends Command {

    Reservation reservation();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableCreateReservation.Builder {
    }

}

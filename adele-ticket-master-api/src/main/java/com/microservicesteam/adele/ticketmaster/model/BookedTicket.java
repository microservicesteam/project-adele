package com.microservicesteam.adele.ticketmaster.model;

import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.BOOKED;

import org.immutables.value.Value;

@Value.Immutable
public interface BookedTicket extends Ticket {
    long bookingId();

    @Override
    default TicketStatus status() {
        return BOOKED;
    }

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableBookedTicket.Builder {
    }

}
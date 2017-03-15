package com.microservicesteam.adele.ticketmaster.model;

import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.PAID;

import org.immutables.value.Value;

@Value.Immutable
public interface PaidTicket extends Ticket {
    long bookingId();

    @Override
    default TicketStatus status() {
        return PAID;
    }

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutablePaidTicket.Builder {
    }

}

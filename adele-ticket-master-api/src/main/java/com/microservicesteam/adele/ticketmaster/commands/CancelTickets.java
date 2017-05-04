package com.microservicesteam.adele.ticketmaster.commands;

import org.immutables.value.Value;

@Value.Immutable
public interface CancelTickets extends TicketsCommand {
    String bookingId();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableCancelTickets.Builder {
    }

}

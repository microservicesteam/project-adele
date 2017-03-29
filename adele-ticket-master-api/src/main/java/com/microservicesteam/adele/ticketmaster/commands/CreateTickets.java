package com.microservicesteam.adele.ticketmaster.commands;

import org.immutables.value.Value;

@Value.Immutable
public interface CreateTickets extends TicketsCommand {

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableCreateTickets.Builder {
    }
}
package com.microservicesteam.adele.ticketmaster.commands;

import java.util.List;

import org.immutables.value.Value;

import com.microservicesteam.adele.ticketmaster.model.Ticket;

@Value.Immutable
public interface CreateTickets extends Command {

    List<Ticket> tickets();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableCreateTickets.Builder {
    }
}

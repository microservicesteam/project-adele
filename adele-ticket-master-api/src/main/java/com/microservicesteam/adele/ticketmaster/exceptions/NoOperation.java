package com.microservicesteam.adele.ticketmaster.exceptions;

import org.immutables.value.Value;

import com.microservicesteam.adele.ticketmaster.commands.TicketsCommand;

@Value.Immutable
public interface NoOperation {

    TicketsCommand sourceCommand();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableNoOperation.Builder {
    }


}

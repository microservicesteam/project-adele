package com.microservicesteam.adele.ticketmaster.events;

import com.microservicesteam.adele.ticketmaster.commands.TicketsCommand;
import org.immutables.value.Value;

@Value.Immutable
public interface TicketsWereAlreadyBooked extends TicketsEvent{

    TicketsCommand sourceCommand();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableTicketsWereAlreadyBooked.Builder {
    }
}

package com.microservicesteam.adele.ticketmaster.events;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.microservicesteam.adele.ticketmaster.model.Ticket;

@Value.Immutable
@JsonSerialize(as = ImmutableTicketsCreated.class)
@JsonDeserialize(as = ImmutableTicketsCreated.class)
@JsonTypeName("TicketsCreated")
public interface TicketsCreated extends Event {

    List<Ticket> tickets();

    static Builder builder(){
        return new Builder();
    }

    class Builder extends ImmutableTicketsCreated.Builder {
    }
}

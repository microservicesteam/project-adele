package com.microservicesteam.adele.ticketmaster.events;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableTicketsCreated.class)
@JsonDeserialize(as = ImmutableTicketsCreated.class)
@JsonTypeName("TicketsCreated")
public interface TicketsCreated extends TicketsEvent {

    static Builder builder(){
        return new Builder();
    }

    class Builder extends ImmutableTicketsCreated.Builder {
    }
}

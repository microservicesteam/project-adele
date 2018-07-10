package com.microservicesteam.adele.ordermanager.domain;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutablePostOrderRequest.class)
@JsonDeserialize(as = ImmutablePostOrderRequest.class)
public interface PostOrderRequest {

    String name();

    String email();

    String reservationId();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutablePostOrderRequest.Builder{
    }

}

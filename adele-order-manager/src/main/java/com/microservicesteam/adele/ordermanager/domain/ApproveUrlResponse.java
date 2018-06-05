package com.microservicesteam.adele.ordermanager.domain;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableApproveUrlResponse.class)
@JsonDeserialize(as = ImmutableApproveUrlResponse.class)
public interface ApproveUrlResponse {

    String approveUrl();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableApproveUrlResponse.Builder{
    }
}

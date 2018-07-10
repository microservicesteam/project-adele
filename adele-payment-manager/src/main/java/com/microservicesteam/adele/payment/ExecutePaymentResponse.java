package com.microservicesteam.adele.payment;

import org.immutables.value.Value;

@Value.Immutable
public interface ExecutePaymentResponse {

    String paymentId();

    ExecutionStatus status();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableExecutePaymentResponse.Builder {
    }

}

package com.microservicesteam.adele.payment;

import org.immutables.value.Value;

@Value.Immutable
public interface ExecutePaymentRequest {

    String paymentId();

    String payerId();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableExecutePaymentRequest.Builder {
    }

}

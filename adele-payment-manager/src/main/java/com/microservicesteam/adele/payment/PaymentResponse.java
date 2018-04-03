package com.microservicesteam.adele.payment;

import org.immutables.value.Value;

@Value.Immutable
public interface PaymentResponse {
    String paymentId();

    String approveUrl();

    PaymentStatus status();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutablePaymentResponse.Builder {
    }

}

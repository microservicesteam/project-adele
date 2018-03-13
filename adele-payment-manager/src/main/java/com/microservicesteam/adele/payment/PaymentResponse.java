package com.microservicesteam.adele.payment;

import java.net.URL;

import org.immutables.value.Value;

@Value.Immutable
public interface PaymentResponse {
    String paymentId();
    URL approveUrl();
    PaymentStatus status();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutablePaymentResponse.Builder {
    }

}

package com.microservicesteam.adele.payment;

import static com.microservicesteam.adele.payment.PaymentStatus.FAILED;

import java.util.Optional;

import org.immutables.value.Value;

@Value.Immutable
public interface PaymentResponse {
    Optional<String> paymentId();

    Optional<String> approveUrl();

    PaymentStatus status();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutablePaymentResponse.Builder {
    }

    static PaymentResponse failed() {
        return PaymentResponse.builder()
                .status(FAILED)
                .build();
    }

}

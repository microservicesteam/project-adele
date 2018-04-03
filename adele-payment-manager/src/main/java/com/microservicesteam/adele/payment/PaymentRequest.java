package com.microservicesteam.adele.payment;

import java.util.Currency;
import java.util.List;

import org.immutables.value.Value;

@Value.Immutable
public interface PaymentRequest {

    String programName();

    String programDescription();

    Currency currency();

    List<Ticket> tickets();

    String returnUrl();

    String cancelUrl();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutablePaymentRequest.Builder {
    }

}

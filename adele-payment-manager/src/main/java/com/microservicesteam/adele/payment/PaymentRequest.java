package com.microservicesteam.adele.payment;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;
import java.util.List;

import org.immutables.value.Value;

@Value.Immutable
public interface PaymentRequest {
    BigDecimal amount();
    Currency currency();
    List<Ticket> tickets();
    URL returnUrl();
    URL cancelUrl();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutablePaymentRequest.Builder {
    }

}

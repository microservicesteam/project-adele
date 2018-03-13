package com.microservicesteam.adele.payment;

import java.math.BigDecimal;
import java.util.Currency;

import org.immutables.value.Value;

@Value.Immutable
public interface Ticket {

    String programName();
    int sector();
    int quantity();
    String description();
    BigDecimal priceAmount();
    Currency currency();

    class Builder extends ImmutableTicket.Builder {
    }

    static Builder builder() {
        return new Builder();
    }

}

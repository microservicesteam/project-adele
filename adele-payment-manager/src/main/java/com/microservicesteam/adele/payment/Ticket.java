package com.microservicesteam.adele.payment;

import java.math.BigDecimal;

import org.immutables.value.Value;

@Value.Immutable
public interface Ticket {

    int sector();

    BigDecimal priceAmount();

    class Builder extends ImmutableTicket.Builder {
    }

    static Builder builder() {
        return new Builder();
    }

}

package com.microservicesteam.adele.event.domain.domain.data;

import com.microservicesteam.adele.event.domain.domain.Price;

import java.math.BigDecimal;

import javax.persistence.Embeddable;

@Embeddable
public class PriceDo {

    public final BigDecimal amount;
    public final String currency;
    
    private PriceDo() {
        this.amount = null;
        this.currency = null;
    }

    private PriceDo(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }


    public Price toImmutable() {
        return Price.builder()
                .withAmount(amount)
                .withCurrency(currency)
                .build();
    }

    public static PriceDo fromImmutable(Price price) {
        return new PriceDo(price.amount(), price.currency());
    }
}

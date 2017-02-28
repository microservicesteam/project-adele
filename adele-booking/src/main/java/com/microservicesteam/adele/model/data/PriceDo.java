package com.microservicesteam.adele.model.data;

import java.math.BigDecimal;

import javax.persistence.Entity;

import com.microservicesteam.adele.model.Price;

@Entity
public class PriceDo extends AbstractDo<Long> {

    public final BigDecimal amount;
    public final String currency;

    public PriceDo() {
        super(null);
        amount = null;
        currency = null;
    }

    private PriceDo(BigDecimal amount, String currency) {
        super(null);
        this.amount = amount;
        this.currency = currency;
    }

    public PriceDo(Long id, BigDecimal amount, String currency) {
        super(id);
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

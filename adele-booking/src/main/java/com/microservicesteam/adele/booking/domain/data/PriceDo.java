package com.microservicesteam.adele.booking.domain.data;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

import com.microservicesteam.adele.booking.domain.Price;

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
                .amount(amount)
                .currency(currency)
                .build();
    }

    public static PriceDo fromImmutable(Price price) {
        return new PriceDo(price.amount(), price.currency());
    }
}

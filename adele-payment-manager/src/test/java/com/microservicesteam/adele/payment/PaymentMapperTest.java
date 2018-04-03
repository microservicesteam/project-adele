package com.microservicesteam.adele.payment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PaymentMapperTest {

    private PaymentMapper mapper = new PaymentMapper();

    @Test
    public void mapRequestToPayment() {

        assertThat(mapper.mapTo(PaymentUtils.paymentRequest()))
                .isEqualTo(PaymentUtils.payment());
    }
}
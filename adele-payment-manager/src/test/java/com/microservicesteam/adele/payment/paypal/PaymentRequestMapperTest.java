package com.microservicesteam.adele.payment.paypal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.microservicesteam.adele.payment.PaymentUtils;

public class PaymentRequestMapperTest {

    private PaymentRequestMapper mapper = new PaymentRequestMapper();

    @Test
    public void mapRequestToPayment() {

        assertThat(mapper.mapTo(PaymentUtils.paymentRequest()))
                .isEqualTo(PaymentUtils.paymentAtRequest());
    }
}
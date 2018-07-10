package com.microservicesteam.adele.payment.paypal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.microservicesteam.adele.payment.PaymentUtils;

public class PaymentResponseMapperTest {

    private PaymentResponseMapper paymentResponseMapper = new PaymentResponseMapper();

    @Test
    public void mapPaymentToPaymentResponse() {
        assertThat(paymentResponseMapper.mapTo(PaymentUtils.paymentAtResponse()))
                .isEqualTo(PaymentUtils.paymentResponse());
    }
}
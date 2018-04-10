package com.microservicesteam.adele.payment.paypal;

import static com.microservicesteam.adele.payment.PaymentUtils.executePaymentResponse;
import static com.microservicesteam.adele.payment.PaymentUtils.paymentAtExecution;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ExecutePaymentResponseMapperTest {

    private ExecutePaymentResponseMapper executePaymentResponseMapper = new ExecutePaymentResponseMapper();

    @Test
    public void mapExecuteRequestToPaymentExecution() {
        Assertions.assertThat(executePaymentResponseMapper.mapTo(paymentAtExecution()))
                .isEqualTo(executePaymentResponse());
    }
}

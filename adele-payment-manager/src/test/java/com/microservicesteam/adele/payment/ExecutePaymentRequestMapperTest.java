package com.microservicesteam.adele.payment;

import org.junit.Test;

import static com.microservicesteam.adele.payment.PaymentUtils.executePaymentRequest;
import static org.assertj.core.api.Assertions.assertThat;

public class ExecutePaymentRequestMapperTest {

    private ExecutePaymentRequestMapper executePaymentRequestMapper = new ExecutePaymentRequestMapper();

    @Test
    public void mapExecuteRequestToPaymentExecution() {
        assertThat(executePaymentRequestMapper.mapTo(executePaymentRequest()))
                .isEqualTo(PaymentUtils.paymentExecution());
    }
}

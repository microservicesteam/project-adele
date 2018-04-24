package com.microservicesteam.adele.payment.paypal;

import static com.microservicesteam.adele.payment.PaymentUtils.executePaymentRequest;
import static com.microservicesteam.adele.payment.PaymentUtils.paymentExecution;
import static org.assertj.core.api.Assertions.assertThat;

import com.paypal.api.payments.Payment;
import org.junit.Test;

public class ExecutePaymentRequestMapperTest {

    private ExecutePaymentRequestMapper executePaymentRequestMapper = new ExecutePaymentRequestMapper();

    @Test
    public void mapExecuteRequestToPaymentExecution() {
        assertThat(executePaymentRequestMapper.mapTo(executePaymentRequest()).getId())
                .isEqualTo(executePaymentRequest().paymentId());
    }
}

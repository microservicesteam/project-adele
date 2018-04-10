package com.microservicesteam.adele.payment.paypal;

import org.springframework.stereotype.Component;

import com.microservicesteam.adele.payment.ExecutePaymentRequest;
import com.paypal.api.payments.PaymentExecution;

@Component
public class ExecutePaymentRequestMapper {

    public PaymentExecution mapTo(ExecutePaymentRequest executePaymentRequest) {
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(executePaymentRequest.payerId());
        return paymentExecution;
    }
}

package com.microservicesteam.adele.payment;

import com.paypal.api.payments.PaymentExecution;

public class ExecutePaymentRequestMapper {

    public PaymentExecution mapTo(ExecutePaymentRequest executePaymentRequest) {
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(executePaymentRequest.payerId());
        return paymentExecution;
    }
}

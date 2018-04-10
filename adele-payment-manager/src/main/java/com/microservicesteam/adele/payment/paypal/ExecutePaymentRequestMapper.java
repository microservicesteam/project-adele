package com.microservicesteam.adele.payment.paypal;

import com.microservicesteam.adele.payment.ExecutePaymentRequest;
import com.paypal.api.payments.PaymentExecution;

public class ExecutePaymentRequestMapper {

    public PaymentExecution mapTo(ExecutePaymentRequest executePaymentRequest) {
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(executePaymentRequest.payerId());
        return paymentExecution;
    }
}

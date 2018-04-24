package com.microservicesteam.adele.payment.paypal;

import com.microservicesteam.adele.payment.ExecutePaymentRequest;
import com.paypal.api.payments.Payment;
import org.springframework.stereotype.Component;

@Component
public class ExecutePaymentRequestMapper {

    public Payment mapTo(ExecutePaymentRequest executePaymentRequest) {
        Payment payment = new Payment();
        payment.setId(executePaymentRequest.paymentId());
        return payment;
    }
}

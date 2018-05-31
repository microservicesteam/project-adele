package com.microservicesteam.adele.payment.paypal;

import org.springframework.stereotype.Component;

import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class PaypalProxy {

    private final APIContext apiContext;

    public Payment create(Payment paymentRequest) throws PayPalRESTException {
        return paymentRequest.create(apiContext);
    }

    public Payment execute(Payment createdPayment, String payerId) throws PayPalRESTException {
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        return createdPayment.execute(apiContext, paymentExecution);
    }

}

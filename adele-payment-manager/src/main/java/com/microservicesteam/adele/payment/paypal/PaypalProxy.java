package com.microservicesteam.adele.payment.paypal;

import org.springframework.stereotype.Component;

import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Component
public class PaypalProxy {

    private final APIContext apiContext;

    public PaypalProxy(PaypalConfig.PaypalProperties paypalProperties) {
        apiContext = new APIContext(
                paypalProperties.getClientId(),
                paypalProperties.getClientSecret(),
                paypalProperties.getMode());
    }

    public Payment create(Payment paymentRequest) throws PayPalRESTException {
        return paymentRequest.create(apiContext);
    }

    public Payment execute(String paymentId, PaymentExecution paymentExecution) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        return payment.execute(apiContext, paymentExecution);
    }
}

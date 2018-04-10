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

    private final PaypalConfig.PaypalProperties paypalProperties;

    public Payment create(Payment paymentRequest) throws PayPalRESTException {

        APIContext apiContext = getApiContext();
        return paymentRequest.create(apiContext);
    }

    public Payment execute(String paymentId, PaymentExecution paymentExecution) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        return payment.execute(getApiContext(), paymentExecution);
    }

    private APIContext getApiContext() {
        return new APIContext(
                paypalProperties.getClientId(),
                paypalProperties.getClientSecret(),
                paypalProperties.getMode());
    }
}

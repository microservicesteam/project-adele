package com.microservicesteam.adele.payment.paypal;

import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class PaypalProxy {

    private final PaypalConfig.PaypalProperties paypalProperties;

    public Payment create(Payment paymentRequest) throws PayPalRESTException {

        APIContext apiContext = getApiContext();
        return paymentRequest.create(apiContext);
    }

    public Payment execute(Payment payment, String payerId) throws PayPalRESTException {
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        return payment.execute(getApiContext(), paymentExecution);
    }

    private APIContext getApiContext() {
        return new APIContext(
                paypalProperties.getClientId(),
                paypalProperties.getClientSecret(),
                paypalProperties.getMode());
    }
}

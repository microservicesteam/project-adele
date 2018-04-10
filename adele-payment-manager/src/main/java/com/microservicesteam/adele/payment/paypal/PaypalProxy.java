package com.microservicesteam.adele.payment.paypal;

import org.springframework.stereotype.Component;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class PaypalProxy {

    private final PaypalConfig.PaypalProperties paypalProperties;

    public Payment create(Payment paymentRequest) throws PayPalRESTException {

        APIContext apiContext = new APIContext(
                paypalProperties.getClientId(),
                paypalProperties.getClientSecret(),
                paypalProperties.getMode());
        return paymentRequest.create(apiContext);
    }
}

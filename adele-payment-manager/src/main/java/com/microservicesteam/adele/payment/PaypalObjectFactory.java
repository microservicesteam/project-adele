package com.microservicesteam.adele.payment;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.APIContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PaypalObjectFactory {

    private final PaypalConfig.PaypalProperties paypalProperties;

    public APIContext getApiContext() {
        return new APIContext(paypalProperties.getClientId(), paypalProperties.getClientSecret(), paypalProperties.getMode());
    }

    public Payment getPayment() {
        return new Payment();
    }

}

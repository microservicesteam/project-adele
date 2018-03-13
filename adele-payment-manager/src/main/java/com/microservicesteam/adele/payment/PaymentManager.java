package com.microservicesteam.adele.payment;

import static com.microservicesteam.adele.payment.PaymentStatus.CREATED;
import static com.microservicesteam.adele.payment.PaymentStatus.FAILED;

import java.net.MalformedURLException;
import java.net.URL;

public class PaymentManager {

    PaymentResponse initiatePayment(PaymentRequest paymentRequest) {

        //TODO: implement intercation with PayPal
        PaymentResponse paymentResponse;

        try {
            paymentResponse = PaymentResponse.builder()
                    .paymentId("dummy-payment-id")
                    .status(CREATED)
                    .approveUrl(new URL("https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=EC-7VA55149MP359292C"))
                    .build();
        } catch (MalformedURLException e) {
            paymentResponse = PaymentResponse.builder()
                    .paymentId("dummy-payment-id")
                    .status(FAILED)
                    .build();
        }

        return paymentResponse;
    }
}

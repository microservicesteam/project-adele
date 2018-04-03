package com.microservicesteam.adele.payment;

import static com.microservicesteam.adele.payment.ExecutionStatus.APPROVED;
import static com.microservicesteam.adele.payment.PaymentStatus.CREATED;

import com.paypal.api.payments.Payer;
import com.paypal.api.payments.RedirectUrls;

public class PaymentManager {

    public PaymentResponse initiatePayment(PaymentRequest paymentRequest) {

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setReturnUrl(paymentRequest.returnUrl());
        redirectUrls.setCancelUrl(paymentRequest.cancelUrl());

        //TODO: implement intercation with PayPal
        return PaymentResponse.builder()
                .paymentId("dummy-payment-id")
                .status(CREATED)
                .approveUrl("https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=EC-7VA55149MP359292C")
                .build();
    }

    public ExecutePaymentResponse executePayment(ExecutePaymentRequest executePaymentRequest) {
        return ExecutePaymentResponse.builder()
                .paymentId("dummy-payment-id")
                .status(APPROVED)
                .build();
    }
}

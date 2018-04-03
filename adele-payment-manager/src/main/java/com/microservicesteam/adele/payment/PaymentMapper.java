package com.microservicesteam.adele.payment;

import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RedirectUrls;

public class PaymentMapper {
    Payment mapTo(PaymentRequest paymentRequest){

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setReturnUrl(paymentRequest.returnUrl());
        redirectUrls.setCancelUrl(paymentRequest.cancelUrl());

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setRedirectUrls(redirectUrls);
        payment.setTransactions(null); //TBD

        return payment;
    }
}

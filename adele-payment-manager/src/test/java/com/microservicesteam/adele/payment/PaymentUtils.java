package com.microservicesteam.adele.payment;

import static java.math.BigDecimal.TEN;

import java.util.Currency;

import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RedirectUrls;

public class PaymentUtils {

    static PaymentRequest paymentRequest() {
        return PaymentRequest.builder()
                .addTickets(Ticket.builder()
                        .sector(1)
                        .priceAmount(TEN)
                        .build())
                .currency(Currency.getInstance("EUR"))
                .programName("Adele Concert 2018")
                .programDescription("Adele Concert 2018 London")
                .returnUrl("http://adeleproject.com/payment?status=success")
                .cancelUrl("http://adeleproject.com/payment?status=failure")
                .build();
    }

    static Payment payment(){
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setReturnUrl("http://adeleproject.com/payment?status=success");
        redirectUrls.setCancelUrl("http://adeleproject.com/payment?status=failure");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setRedirectUrls(redirectUrls);
        payment.setTransactions(null); //TBD

        return payment;

    }
}

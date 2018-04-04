package com.microservicesteam.adele.payment;

import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;

import java.util.Currency;

import static com.microservicesteam.adele.payment.ExecutionStatus.APPROVED;
import static com.microservicesteam.adele.payment.ExecutionStatus.FAILED;
import static java.math.BigDecimal.TEN;

public class PaymentUtils {

    private static final String PAYER_ID = "payerId";
    private static final String PAYMENT_ID = "paymentId";

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

    static ExecutePaymentRequest executePaymentRequest() {
        return ExecutePaymentRequest.builder()
                .payerId(PAYER_ID)
                .paymentId(PAYMENT_ID)
                .build();
    }

    static PaymentExecution paymentExecution() {
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(PAYER_ID);
        return paymentExecution;
    }

    static ExecutePaymentResponse executePaymentResponse() {
        return ExecutePaymentResponse.builder()
                .paymentId(PAYMENT_ID)
                .status(APPROVED)
                .build();
    }

    static ExecutePaymentResponse failedExecutePaymentResponse() {
        return ExecutePaymentResponse.builder()
                .paymentId(PAYMENT_ID)
                .status(FAILED)
                .build();
    }
}

package com.microservicesteam.adele.payment;

import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;

import static com.microservicesteam.adele.payment.ExecutionStatus.APPROVED;
import static com.microservicesteam.adele.payment.ExecutionStatus.FAILED;
import static com.microservicesteam.adele.payment.PaymentStatus.CREATED;

@AllArgsConstructor
public class PaymentManager {
    private final PaypalObjectFactory paypalObjectFactory;
    private final ExecutePaymentRequestMapper executePaymentRequestMapper;


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
        PaymentExecution paymentExecution = executePaymentRequestMapper.mapTo(executePaymentRequest);

        Payment payment = paypalObjectFactory.getPayment();
        payment.setId(executePaymentRequest.paymentId());

        try {
            Payment executedPayment = payment.execute(paypalObjectFactory.getApiContext(), paymentExecution);

            //TODO ExecutePaymentResponseMapper?
            return ExecutePaymentResponse.builder()
                    .paymentId(executedPayment.getId())
                    .status(executedPayment.getState().equals("approved") ? APPROVED : FAILED)
                    .build();
        } catch (PayPalRESTException e) {
            return ExecutePaymentResponse.builder()
                    .paymentId(executePaymentRequest.paymentId())
                    .status(FAILED)
                    .build();
        }
    }
}

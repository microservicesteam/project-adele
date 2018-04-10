package com.microservicesteam.adele.payment;

import static com.microservicesteam.adele.payment.ExecutionStatus.APPROVED;
import static com.microservicesteam.adele.payment.ExecutionStatus.FAILED;

import com.microservicesteam.adele.payment.paypal.PaymentRequestMapper;
import com.microservicesteam.adele.payment.paypal.PaymentResponseMapper;
import com.microservicesteam.adele.payment.paypal.PaypalProxy;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class PaymentManager {
    private final ExecutePaymentRequestMapper executePaymentRequestMapper;

    private final PaymentRequestMapper paymentRequestMapper;
    private final PaymentResponseMapper paymentResponseMapper;
    private final PaypalProxy paypalProxy;

    public PaymentResponse initiatePayment(PaymentRequest paymentRequest) {

        Payment payment = paymentRequestMapper.mapTo(paymentRequest);
        try {

            Payment createdPayment = paypalProxy.create(payment);
            log.debug("Payment created successfully at PayPal");
            return paymentResponseMapper.mapTo(createdPayment);

        } catch (PayPalRESTException e) {
            log.error("Error at PayPal", e);
            return PaymentResponse.failed();

        } catch (Exception ex) {
            log.error("Error at Adele", ex);
            return PaymentResponse.failed();
        }

    }

    public ExecutePaymentResponse executePayment(ExecutePaymentRequest executePaymentRequest) {
        PaymentExecution paymentExecution = executePaymentRequestMapper.mapTo(executePaymentRequest);

        try {
            Payment executedPayment = paypalProxy.execute(executePaymentRequest.paymentId(), paymentExecution);

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

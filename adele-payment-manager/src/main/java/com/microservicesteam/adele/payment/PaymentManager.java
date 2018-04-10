package com.microservicesteam.adele.payment;

import static com.microservicesteam.adele.payment.ExecutionStatus.FAILED;

import org.springframework.stereotype.Component;

import com.microservicesteam.adele.payment.paypal.ExecutePaymentRequestMapper;
import com.microservicesteam.adele.payment.paypal.ExecutePaymentResponseMapper;
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
@Component
public class PaymentManager {

    private final PaymentRequestMapper paymentRequestMapper;
    private final PaymentResponseMapper paymentResponseMapper;
    private final PaypalProxy paypalProxy;

    private final ExecutePaymentRequestMapper executePaymentRequestMapper;
    private final ExecutePaymentResponseMapper executePaymentResponseMapper;

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
            log.debug("Payment executed successfully at PayPal");
            return executePaymentResponseMapper.mapTo(executedPayment);
        } catch (PayPalRESTException e) {
            log.error("Error at PayPal", e);
            return ExecutePaymentResponse.builder()
                    .paymentId(executePaymentRequest.paymentId())
                    .status(FAILED)
                    .build();
        } catch (Exception ex) {
            log.error("Error at Adele", ex);
            return ExecutePaymentResponse.builder()
                    .paymentId(executePaymentRequest.paymentId())
                    .status(FAILED)
                    .build();
        }
    }

}

package com.microservicesteam.adele.payment;

import com.microservicesteam.adele.payment.paypal.*;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.microservicesteam.adele.payment.ExecutionStatus.FAILED;

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
        try {
            Payment payment = paymentRequestMapper.mapTo(paymentRequest);
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
        try {
            Payment payment = executePaymentRequestMapper.mapTo(executePaymentRequest);
            Payment executedPayment = paypalProxy.execute(payment, executePaymentRequest.payerId());
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

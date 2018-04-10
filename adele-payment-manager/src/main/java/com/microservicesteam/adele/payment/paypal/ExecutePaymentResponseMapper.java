package com.microservicesteam.adele.payment.paypal;

import static com.microservicesteam.adele.payment.ExecutionStatus.APPROVED;
import static com.microservicesteam.adele.payment.ExecutionStatus.FAILED;

import org.springframework.stereotype.Component;

import com.microservicesteam.adele.payment.ExecutePaymentResponse;
import com.paypal.api.payments.Payment;

@Component
public class ExecutePaymentResponseMapper {

    public static final String STATUS_APPROVED = "approved";

    public ExecutePaymentResponse mapTo(Payment executedPayment) {
        return ExecutePaymentResponse.builder()
                .paymentId(executedPayment.getId())
                .status(executedPayment.getState().equals(STATUS_APPROVED) ? APPROVED : FAILED)
                .build();
    }


}

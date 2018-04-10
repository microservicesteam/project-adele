package com.microservicesteam.adele.payment.paypal;

import static com.microservicesteam.adele.payment.PaymentStatus.FAILED;

import org.springframework.stereotype.Component;

import com.microservicesteam.adele.payment.PaymentResponse;
import com.microservicesteam.adele.payment.PaymentStatus;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;

@Component
public class PaymentResponseMapper {

    private static final String CREATED = "created";
    private static final String APPROVAL_URL = "approval_url";

    public PaymentResponse mapTo(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .status(CREATED.equalsIgnoreCase(payment.getState()) ? PaymentStatus.CREATED : FAILED)
                .approveUrl(payment.getLinks().stream()
                        .filter(links -> APPROVAL_URL.equalsIgnoreCase(links.getRel()))
                        .findFirst()
                        .map(Links::getHref))
                .build();
    }
}

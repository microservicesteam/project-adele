package com.microservicesteam.adele.payment.paypal;

import static com.microservicesteam.adele.payment.PaymentStatus.CREATED;
import static com.microservicesteam.adele.payment.PaymentStatus.FAILED;

import com.microservicesteam.adele.payment.PaymentResponse;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;

public class PaymentResponseMapper {
    public PaymentResponse mapTo(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .status("created".equalsIgnoreCase(payment.getState()) ? CREATED : FAILED)
                .approveUrl(payment.getLinks().stream()
                        .filter(links -> "approval_url".equalsIgnoreCase(links.getRel()))
                        .findFirst()
                        .map(Links::getHref))
                .build();
    }
}

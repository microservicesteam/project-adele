package com.microservicesteam.adele.payment;

import static com.microservicesteam.adele.payment.PaymentStatus.CREATED;
import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;

public class PaymentManagerTest {

    private PaymentManager paymentManager;

    @Before
    public void setUp() {
        paymentManager = new PaymentManager();
    }

    @Test
    public void succesfulInitPayment() {
        PaymentResponse paymentResponse = paymentManager.initiatePayment(PaymentUtils.paymentRequest());

        assertThat(paymentResponse)
                .isEqualTo(PaymentResponse.builder()
                        .paymentId("dummy-payment-id")
                        .status(CREATED)
                        .approveUrl("https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=EC-7VA55149MP359292C")
                        .build());
    }
}
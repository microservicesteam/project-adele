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
    public void setUp() throws Exception {
        paymentManager = new PaymentManager();
    }

    @Test
    public void succesfulInitPayment() throws MalformedURLException {
        PaymentResponse paymentResponse = paymentManager.initiatePayment(PaymentRequest.builder()
                .addTickets(Ticket.builder()
                        .sector(1)
                        .priceAmount(TEN)
                        .build())
                .currency(Currency.getInstance("EUR"))
                .programName("Adele Concert 2018")
                .programDescription("Adele Concert 2018 London")
                .returnUrl(new URL("http://adeleproject.com/payment?status=success"))
                .cancelUrl(new URL("http://adeleproject.com/payment?status=failure"))
                .build());

        assertThat(paymentResponse)
                .isEqualTo(PaymentResponse.builder()
                        .paymentId("dummy-payment-id")
                        .status(CREATED)
                        .approveUrl(new URL("https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=EC-7VA55149MP359292C"))
                        .build());
    }
}
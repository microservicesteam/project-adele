package com.microservicesteam.adele.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.microservicesteam.adele.payment.paypal.PaymentRequestMapper;
import com.microservicesteam.adele.payment.paypal.PaymentResponseMapper;
import com.microservicesteam.adele.payment.paypal.PaypalProxy;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

@RunWith(SpringRunner.class)
public class PaymentManagerTest {

    private static final PaymentRequest PAYMENT_REQUEST = PaymentUtils.paymentRequest();
    private static final Payment PAYMENT_AT_REQUEST = PaymentUtils.paymentAtRequest();
    private static final Payment PAYMENT_AT_RESPONSE = PaymentUtils.paymentAtResponse();

    @MockBean
    private PaymentRequestMapper paymentRequestMapper;
    @MockBean
    private PaymentResponseMapper paymentResponseMapper;
    @MockBean
    private PaypalProxy paypalProxy;

    private PaymentManager paymentManager;

    @Before
    public void setUp() {
        paymentManager = new PaymentManager(paymentRequestMapper, paymentResponseMapper, paypalProxy);
    }

    @Test
    public void successfulInitPayment() throws PayPalRESTException {
        PaymentResponse expectedPaymentResponse = PaymentUtils.paymentResponse();
        when(paymentRequestMapper.mapTo(PAYMENT_REQUEST)).thenReturn(PAYMENT_AT_REQUEST);
        when(paypalProxy.create(PAYMENT_AT_REQUEST)).thenReturn(PAYMENT_AT_RESPONSE);
        when(paymentResponseMapper.mapTo(PAYMENT_AT_RESPONSE)).thenReturn(expectedPaymentResponse);

        PaymentResponse paymentResponse = paymentManager.initiatePayment(PAYMENT_REQUEST);

        assertThat(paymentResponse).isEqualTo(expectedPaymentResponse);
    }

    @Test
    public void failedInitPaymentDueAtPaypal() throws PayPalRESTException {
        when(paymentRequestMapper.mapTo(PAYMENT_REQUEST)).thenReturn(PAYMENT_AT_REQUEST);
        when(paypalProxy.create(PAYMENT_AT_REQUEST)).thenThrow(new PayPalRESTException(""));

        PaymentResponse paymentResponse = paymentManager.initiatePayment(PAYMENT_REQUEST);

        assertThat(paymentResponse)
                .isEqualTo(PaymentResponse.failed());
    }

    @Test
    public void failedInitPaymentDueAtAdele() throws PayPalRESTException {
        when(paymentRequestMapper.mapTo(PAYMENT_REQUEST)).thenReturn(PAYMENT_AT_REQUEST);
        when(paypalProxy.create(PAYMENT_AT_REQUEST)).thenThrow(new NullPointerException(""));

        PaymentResponse paymentResponse = paymentManager.initiatePayment(PAYMENT_REQUEST);

        assertThat(paymentResponse)
                .isEqualTo(PaymentResponse.failed());
    }
}
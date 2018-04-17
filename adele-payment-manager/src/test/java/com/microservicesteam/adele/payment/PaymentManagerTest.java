package com.microservicesteam.adele.payment;

import static com.microservicesteam.adele.payment.PaymentUtils.executePaymentRequest;
import static com.microservicesteam.adele.payment.PaymentUtils.executePaymentResponse;
import static com.microservicesteam.adele.payment.PaymentUtils.failedExecutePaymentResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.microservicesteam.adele.payment.paypal.ExecutePaymentRequestMapper;
import com.microservicesteam.adele.payment.paypal.ExecutePaymentResponseMapper;
import com.microservicesteam.adele.payment.paypal.PaymentRequestMapper;
import com.microservicesteam.adele.payment.paypal.PaymentResponseMapper;
import com.microservicesteam.adele.payment.paypal.PaypalProxy;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.base.rest.PayPalRESTException;

@RunWith(MockitoJUnitRunner.class)
public class PaymentManagerTest {

    private static final PaymentRequest PAYMENT_REQUEST = PaymentUtils.paymentRequest();
    private static final Payment PAYMENT_AT_REQUEST = PaymentUtils.paymentAtRequest();
    private static final Payment PAYMENT_AT_RESPONSE = PaymentUtils.paymentAtResponse();

    @Mock
    private PaymentRequestMapper paymentRequestMapper;
    @Mock
    private PaymentResponseMapper paymentResponseMapper;
    @Mock
    private PaypalProxy paypalProxy;
    @Mock
    private ExecutePaymentRequestMapper executePaymentRequestMapper;
    @Mock
    private ExecutePaymentResponseMapper executePaymentResponseMapper;

    private PaymentManager paymentManager;

    @Before
    public void setUp() {
        paymentManager = new PaymentManager(paymentRequestMapper, paymentResponseMapper, paypalProxy, executePaymentRequestMapper, executePaymentResponseMapper);
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
    public void failedInitPaymentDueToPaypal() throws PayPalRESTException {
        when(paymentRequestMapper.mapTo(PAYMENT_REQUEST)).thenReturn(PAYMENT_AT_REQUEST);
        when(paypalProxy.create(PAYMENT_AT_REQUEST)).thenThrow(new PayPalRESTException(""));

        PaymentResponse paymentResponse = paymentManager.initiatePayment(PAYMENT_REQUEST);

        assertThat(paymentResponse)
                .isEqualTo(PaymentResponse.failed());
    }

    @Test
    public void failedInitPaymentDueToAdele() throws PayPalRESTException {
        when(paymentRequestMapper.mapTo(PAYMENT_REQUEST)).thenReturn(PAYMENT_AT_REQUEST);
        when(paypalProxy.create(PAYMENT_AT_REQUEST)).thenThrow(new NullPointerException(""));

        PaymentResponse paymentResponse = paymentManager.initiatePayment(PAYMENT_REQUEST);
        assertThat(paymentResponse)
                .isEqualTo(PaymentResponse.failed());
    }

    @Test
    public void executePaymentShouldReturnWithApprovedResponseWhenExecutedPaymentHasApprovedState() throws PayPalRESTException {
        //given
        Payment executedPayment = new Payment();
        executedPayment.setId("paymentId");
        executedPayment.setState("approved");
        when(paypalProxy.execute(any(Payment.class), any(String.class))).thenReturn(executedPayment);
        when(executePaymentResponseMapper.mapTo(any())).thenReturn(executePaymentResponse());

        //when
        ExecutePaymentResponse executePaymentResponse = paymentManager.executePayment(executePaymentRequest());

        //then
        assertThat(executePaymentResponse).isEqualTo(executePaymentResponse());
    }

    @Test
    public void executePaymentShouldReturnWithFailedResponseWhenPaymentExecutionThrowsRestException() throws PayPalRESTException {
        //given
        when(paypalProxy.execute(any(Payment.class), any(String.class))).thenThrow(new PayPalRESTException("test rest failure"));

        //when
        ExecutePaymentResponse executePaymentResponse = paymentManager.executePayment(executePaymentRequest());

        //then
        assertThat(executePaymentResponse).isEqualTo(failedExecutePaymentResponse());
    }
}
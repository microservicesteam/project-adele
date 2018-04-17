package com.microservicesteam.adele.payment.paypal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@RunWith(MockitoJUnitRunner.class)
public class PaypalProxyTest {

    private static final String CLIENT_ID = "clientId";
    private static final String CLIENT_SECRET = "clientSecret";
    private static final String MODE = "sandbox";
    @Captor
    private ArgumentCaptor<APIContext> apiContextCaptor;
    @Captor
    private ArgumentCaptor<PaymentExecution> paymentExecutionCaptor;

    @Mock
    private Payment paymentRequest;

    private PaypalProxy paypalProxy;

    @Before
    public void setUp() {
        PaypalConfig.PaypalProperties paypalProperties = new PaypalConfig.PaypalProperties();
        paypalProperties.setClientId(CLIENT_ID);
        paypalProperties.setClientSecret(CLIENT_SECRET);
        paypalProperties.setMode(MODE);

        paypalProxy = new PaypalProxy(paypalProperties);
    }

    @Test
    public void create() throws PayPalRESTException {
        Payment expectedPayment = new Payment();
        when(paymentRequest.create(any(APIContext.class))).thenReturn(expectedPayment);

        Payment payment = paypalProxy.create(paymentRequest);

        verify(paymentRequest, times(1)).create(apiContextCaptor.capture());
        assertThat(apiContextCaptor.getValue().getClientID()).isEqualTo(CLIENT_ID);
        assertThat(apiContextCaptor.getValue().getClientSecret()).isEqualTo(CLIENT_SECRET);
        assertThat(expectedPayment).isEqualTo(payment);
    }

    @Test
    public void execute() throws PayPalRESTException {
        Payment expectedPayment = new Payment();
        when(paymentRequest.execute(any(APIContext.class), any(PaymentExecution.class))).thenReturn(expectedPayment);

        Payment payment = paypalProxy.execute(paymentRequest, "payerId");

        verify(paymentRequest, times(1)).execute(apiContextCaptor.capture(), paymentExecutionCaptor.capture());
        assertThat(apiContextCaptor.getValue().getClientID()).isEqualTo(CLIENT_ID);
        assertThat(apiContextCaptor.getValue().getClientSecret()).isEqualTo(CLIENT_SECRET);
        assertThat(paymentExecutionCaptor.getValue().getPayerId()).isEqualTo("payerId");
        assertThat(expectedPayment).isEqualTo(payment);
    }
}
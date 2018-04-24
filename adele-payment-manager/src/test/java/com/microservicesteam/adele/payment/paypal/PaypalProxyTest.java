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

    @Captor
    private ArgumentCaptor<PaymentExecution> paymentExecutionCaptor;

    @Mock
    private Payment paymentRequest;

    @Mock
    private APIContext apiContext;

    private PaypalProxy paypalProxy;

    @Before
    public void setUp() {

        paypalProxy = new PaypalProxy(apiContext);
    }

    @Test
    public void create() throws PayPalRESTException {
        Payment expectedPayment = new Payment();
        when(paymentRequest.create(any(APIContext.class))).thenReturn(expectedPayment);

        Payment payment = paypalProxy.create(paymentRequest);

        verify(paymentRequest, times(1)).create(any(APIContext.class));
        assertThat(expectedPayment).isEqualTo(payment);
    }

    @Test
    public void execute() throws PayPalRESTException {
        Payment expectedPayment = new Payment();
        when(paymentRequest.execute(any(APIContext.class), any(PaymentExecution.class))).thenReturn(expectedPayment);

        Payment payment = paypalProxy.execute(paymentRequest, "payerId");

        verify(paymentRequest, times(1)).execute(any(APIContext.class), paymentExecutionCaptor.capture());
        assertThat(paymentExecutionCaptor.getValue().getPayerId()).isEqualTo("payerId");
        assertThat(expectedPayment).isEqualTo(payment);
    }
}
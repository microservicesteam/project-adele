package com.microservicesteam.adele.payment.paypal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@RunWith(SpringRunner.class)
public class PaypalProxyTest {

    private static final String CLIENT_ID = "clientId";
    private static final String CLIENT_SECRET = "clientSecret";
    private static final String MODE = "sandbox";
    private static APIContext apiContext = new APIContext("clientId", "clientSecret", "sandbox");

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
        //TODO: compare apiContext as well
        when(paymentRequest.create(any(APIContext.class))).thenReturn(expectedPayment);

        Payment payment = paypalProxy.create(paymentRequest);

        verify(paymentRequest, times(1)).create(any(APIContext.class));
        assertThat(expectedPayment).isEqualTo(payment);
    }
}
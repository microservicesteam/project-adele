package com.microservicesteam.adele.payment;

import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.microservicesteam.adele.payment.PaymentStatus.CREATED;
import static com.microservicesteam.adele.payment.PaymentUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PaymentManagerTest {

    @Mock
    private PaypalObjectFactory objectFactory;

    @Mock
    private ExecutePaymentRequestMapper executePaymentRequestMapper;

    @Mock
    private Payment payment;

    private PaymentManager paymentManager;

    @Before
    public void setUp() {
        when(objectFactory.getPayment()).thenReturn(payment); //every time when a mock returns with another mock a kitten dies in the universe...
        paymentManager = new PaymentManager(objectFactory, executePaymentRequestMapper);
    }

    @Test
    public void successfulInitPayment() {
        PaymentResponse paymentResponse = paymentManager.initiatePayment(PaymentUtils.paymentRequest());

        assertThat(paymentResponse)
                .isEqualTo(PaymentResponse.builder()
                        .paymentId("dummy-payment-id")
                        .status(CREATED)
                        .approveUrl("https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=EC-7VA55149MP359292C")
                        .build());
    }

    @Test
    public void executePaymentShouldReturnWithApprovedResponseWhenExecutedPaymentHasApprovedState() throws PayPalRESTException {
        //given
        Payment executedPayment = new Payment();
        executedPayment.setId("paymentId");
        executedPayment.setState("approved");
        when(payment.execute(any(APIContext.class), any(PaymentExecution.class))).thenReturn(executedPayment);

        //when
        ExecutePaymentResponse executePaymentResponse = paymentManager.executePayment(executePaymentRequest());

        //then
        assertThat(executePaymentResponse).isEqualTo(executePaymentResponse());
    }

    @Test
    public void executePaymentShouldReturnWithFailedResponseWhenPaymentExecutionThrowsRestException() throws PayPalRESTException {
        //given
        when(payment.execute(any(APIContext.class), any(PaymentExecution.class))).thenThrow(new PayPalRESTException("test rest failure"));

        //when
        ExecutePaymentResponse executePaymentResponse = paymentManager.executePayment(executePaymentRequest());

        //then
        assertThat(executePaymentResponse).isEqualTo(failedExecutePaymentResponse());
    }
}
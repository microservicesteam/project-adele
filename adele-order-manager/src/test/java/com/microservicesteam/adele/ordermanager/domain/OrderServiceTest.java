package com.microservicesteam.adele.ordermanager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Matchers.any;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;

import com.google.common.eventbus.EventBus;
import com.microservicesteam.adele.messaging.listeners.DeadEventListener;
import com.microservicesteam.adele.ordermanager.domain.exception.InvalidPaymentResponseException;
import com.microservicesteam.adele.payment.PaymentManager;
import com.microservicesteam.adele.payment.PaymentResponse;
import com.microservicesteam.adele.payment.PaymentStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String RESERVATION_ID = "6ad1be54-5748-4e50-9aaa-e3ef2c4679c2";
    private static final String ORDER_ID = "2ba221a9-29c3-4379-b138-dbe18e468502";
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final String APPROVE_URL = "approve url";
    private static final String PAYMENT_ID = "payment id";
    private static final String PAYMENT_INITIATION_FAILED_TO_ORDER_ID =
            "Payment initiation failed to orderId: " + ORDER_ID;
    private static final String PAYMENT_ID_MISSING_TO_ORDER_ID = "Payment id missing to orderId: " + ORDER_ID;
    private static final String APPROVE_URL_IS_MISSING_TO_ORDER_ID = "Approve url is missing to orderId: " + ORDER_ID;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private Supplier<String> idGenerator;

    @Mock
    private Supplier<LocalDateTime> currentLocalDateTime;

    @Mock
    private PaymentManager paymentManager;

    private OrderService orderService;

    private EventBus eventBus;
    private DeadEventListener deadEventListener;

    @Before
    public void setUp() {
        eventBus = new EventBus();
        deadEventListener = new DeadEventListener(eventBus);
        deadEventListener.init();
        orderService = new OrderService(orderRepository, reservationRepository, paymentManager, idGenerator, currentLocalDateTime, eventBus);

        when(idGenerator.get()).thenReturn(ORDER_ID);
        when(currentLocalDateTime.get()).thenReturn(NOW);
    }

    @Test
    public void saveOrderShouldReturnTheSavedOrderId() {
        PostOrderRequest postOrderRequest = givenPostOrderRequest();
        Order order = givenOrder();
        when(orderRepository.save(order)).thenReturn(order);

        String actual = orderService.saveOrder(postOrderRequest);

        verify(orderRepository).save(order);
        assertThat(actual).isEqualTo(ORDER_ID);
    }

    @Test
    public void initiatePaymentShouldReturnApproveUrlWhenPaymentResponseIsCorrect() throws Exception {
        PaymentResponse paymentResponse = PaymentResponse.builder()
                .status(PaymentStatus.CREATED)
                .paymentId(PAYMENT_ID)
                .approveUrl(APPROVE_URL)
                .build();
        ApproveUrlResponse expected = ApproveUrlResponse.builder()
                .approveUrl(APPROVE_URL)
                .build();
        when(paymentManager.initiatePayment(any())).thenReturn(paymentResponse);

        ApproveUrlResponse actual = orderService.initiatePayment(ORDER_ID);

        verify(paymentManager).initiatePayment(any());
        verify(orderRepository).updatePaymentId(ORDER_ID, PAYMENT_ID);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void initiatePaymentShouldThrowInvalidPaymentResponseExceptionWhenPaymentStatusIsFailed() throws Exception {
        PaymentResponse paymentResponse = PaymentResponse.builder()
                .status(PaymentStatus.FAILED)
                .paymentId(PAYMENT_ID)
                .approveUrl(APPROVE_URL)
                .build();
        when(paymentManager.initiatePayment(any())).thenReturn(paymentResponse);

        Throwable actual = catchThrowable( () ->orderService.initiatePayment(ORDER_ID));

        verify(paymentManager).initiatePayment(any());
        assertThat(actual).isInstanceOf(InvalidPaymentResponseException.class);
        assertThat(actual).hasMessage(PAYMENT_INITIATION_FAILED_TO_ORDER_ID);
    }

    @Test
    public void initiatePaymentShouldThrowInvalidPaymentResponseExceptionWhenPaymentIdIsEmpty() throws Exception {
        PaymentResponse paymentResponse = PaymentResponse.builder()
                .status(PaymentStatus.CREATED)
                .paymentId(Optional.empty())
                .approveUrl(APPROVE_URL)
                .build();
        when(paymentManager.initiatePayment(any())).thenReturn(paymentResponse);

        Throwable actual = catchThrowable( () ->orderService.initiatePayment(ORDER_ID));

        verify(paymentManager).initiatePayment(any());
        assertThat(actual).isInstanceOf(InvalidPaymentResponseException.class);
        assertThat(actual).hasMessage(PAYMENT_ID_MISSING_TO_ORDER_ID);
    }

    @Test
    public void initiatePaymentShouldThrowInvalidPaymentResponseExceptionWhenApproveUrlIsEmpty() throws Exception {
        PaymentResponse paymentResponse = PaymentResponse.builder()
                .status(PaymentStatus.CREATED)
                .paymentId(PAYMENT_ID)
                .approveUrl(Optional.empty())
                .build();
        when(paymentManager.initiatePayment(any())).thenReturn(paymentResponse);

        Throwable actual = catchThrowable( () ->orderService.initiatePayment(ORDER_ID));

        verify(paymentManager).initiatePayment(any());
        assertThat(actual).isInstanceOf(InvalidPaymentResponseException.class);
        assertThat(actual).hasMessage(APPROVE_URL_IS_MISSING_TO_ORDER_ID);
    }

    private PostOrderRequest givenPostOrderRequest() {
        return PostOrderRequest.builder()
                .name(NAME)
                .email(EMAIL)
                .reservationId(RESERVATION_ID)
                .build();
    }

    private Order givenOrder() {
        return Order.builder()
                .orderId(ORDER_ID)
                .reservationId(RESERVATION_ID)
                .email(EMAIL)
                .name(NAME)
                .payerId(null)
                .paymentId(null)
                .status(OrderStatus.RESERVED)
                .creationTimestamp(NOW)
                .lastUpdated(NOW)
                .build();
    }

}
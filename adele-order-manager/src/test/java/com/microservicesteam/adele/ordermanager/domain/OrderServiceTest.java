package com.microservicesteam.adele.ordermanager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.function.Supplier;

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

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private Supplier<String> idGenerator;

    @Mock Supplier<LocalDateTime> currentLocalDateTime;

    private OrderService orderService;

    @Before
    public void setUp() {
        orderService = new OrderService(orderRepository, idGenerator, currentLocalDateTime);
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
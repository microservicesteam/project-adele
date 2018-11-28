package com.microservicesteam.adele.ordermanager.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class OrderRepositoryTest {

    public static final String ORDER_ID_1 = "orderId1";
    public static final String PAYMENT_ID_1 = "paymentId1";
    public static final String RESERVATION_ID = "reservationId";
    public static final LocalDateTime NOW = LocalDateTime.now();
    public static final String PAYER_ID = "payerId";
    public static final String EMAIL = "email";
    public static final String NAME = "name";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void updatePaymentIdShouldUpdatePaymentId() {
        entityManager.persist(givenOrder(ORDER_ID_1, OrderStatus.PAYMENT_CREATED, null));

        orderRepository.updatePaymentIdByOrderId(ORDER_ID_1, PAYMENT_ID_1);

        entityManager.clear();
        Order actual = entityManager.find(Order.class, ORDER_ID_1);
        Order expected = givenOrder(ORDER_ID_1, OrderStatus.PAYMENT_CREATED, PAYMENT_ID_1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void updateStatusByOrderIdAndStatus() {
        entityManager.persist(givenOrder(ORDER_ID_1, OrderStatus.PAYMENT_CREATED, PAYMENT_ID_1));

        orderRepository.updateStatusByOrderId(ORDER_ID_1, OrderStatus.PAYMENT_APPROVED);

        entityManager.clear();
        Order actual = entityManager.find(Order.class, ORDER_ID_1);
        Order expected = givenOrder(ORDER_ID_1, OrderStatus.PAYMENT_APPROVED, PAYMENT_ID_1);
        assertThat(actual).isEqualTo(expected);
    }

//    @Test
//    public void updateStatusByOrderIdPaymentIdStatus() throws Exception {
//        orderRepository.save(givenOrder(ORDER_ID_1, OrderStatus.PAYMENT_CREATED, null));
//
//        //orderRepository.updateStatusByOrderIdPaymentIdStatus(ORDER_ID_1, PAYMENT_ID_1);
//
//        Order actual = orderRepository.findOne(ORDER_ID_1);
//        Order expected = givenOrder(ORDER_ID_1, OrderStatus.PAYMENT_CREATED, PAYMENT_ID_1);
//        assertThat(actual).isEqualTo(expected);
//    }

    private Order givenOrder(String orderId, OrderStatus status, String paymentId) {
        return Order.builder()
                .orderId(orderId)
                .reservationId(RESERVATION_ID)
                .status(status)
                .creationTimestamp(NOW)
                .name(NAME)
                .email(EMAIL)
                .paymentId(paymentId)
                .payerId(PAYER_ID)
                .lastUpdated(NOW)
                .build();
    }

    @SpringBootApplication(scanBasePackages = "com.microservicesteam.adele.ordermanager.domain")
    static class TestConfiguration {
    }

}
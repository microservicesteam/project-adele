package com.microservicesteam.adele.ordermanager.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import com.microservicesteam.adele.ordermanager.domain.exception.InvalidPaymentResponseException;
import com.microservicesteam.adele.payment.PaymentManager;
import com.microservicesteam.adele.payment.PaymentRequest;
import com.microservicesteam.adele.payment.PaymentResponse;
import com.microservicesteam.adele.payment.PaymentStatus;
import com.microservicesteam.adele.payment.Ticket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private static final String REDIRECT_PATH = "/orders/%s/payment?status=%s";
    private static final String APPROVED = "approved";
    private static final String CANCELLED = "cancelled";

    private final OrderConfiguration.OrderProperties orderProperties;
    private final OrderRepository orderRepository;
    private final PaymentManager paymentManager;
    private final Supplier<String> orderIdGenerator;
    private final Supplier<LocalDateTime> currentLocalDateTime;

    public String saveOrder(PostOrderRequest orderRequest) {
        Order order = orderRepository.save(fromPostOrderRequest(orderRequest));
        return order.orderId;
    }

    public ApproveUrlResponse initiatePayment(String orderId) {
        PaymentResponse paymentResponse = paymentManager.initiatePayment(createPaymentRequest(orderId));

        if (paymentResponse.status().equals(PaymentStatus.FAILED)) {
            throw new InvalidPaymentResponseException("Payment initiation failed to orderId: " + orderId);
        }

        String paymentId = paymentResponse.paymentId().orElseThrow(() -> new InvalidPaymentResponseException("Payment id missing to orderId: " + orderId));
        orderRepository.updatePaymentId(orderId, paymentId);

        String approveUrl = paymentResponse.approveUrl().orElseThrow(() -> new InvalidPaymentResponseException("Approve url is missing to orderId: " + orderId));
        return ApproveUrlResponse.builder()
                .approveUrl(approveUrl)
                .build();
    }

    private Order fromPostOrderRequest(PostOrderRequest postOrderRequest) {
        LocalDateTime now = currentLocalDateTime.get();
        return Order.builder()
                .orderId(orderIdGenerator.get())
                .reservationId(postOrderRequest.reservationId())
                .name(postOrderRequest.name())
                .email(postOrderRequest.email())
                .status(OrderStatus.RESERVED)
                .creationTimestamp(now)
                .lastUpdated(now)
                .build();
    }

    private PaymentRequest createPaymentRequest(String orderId) {
        // TODO query necessary data from db
        return PaymentRequest.builder()
                .programName("Adele Concert 2018, Wembley")
                .programDescription("Best concert ever")
                .currency(Currency.getInstance("HUF"))
                .addTickets(
                        Ticket.builder()
                                .sector(1)
                                .priceAmount(BigDecimal.TEN)
                                .build(),
                        Ticket.builder()
                                .sector(1)
                                .priceAmount(BigDecimal.TEN)
                                .build())
                .returnUrl(String.format(orderProperties.getDomainUrl() + REDIRECT_PATH, orderId, APPROVED))
                .cancelUrl(String.format(orderProperties.getDomainUrl() + REDIRECT_PATH, orderId, CANCELLED))
                .build();
    }

}


package com.microservicesteam.adele.ordermanager.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;
import com.microservicesteam.adele.messaging.EventBasedService;
import com.microservicesteam.adele.ordermanager.domain.exception.InvalidPaymentResponseException;
import com.microservicesteam.adele.payment.ExecutePaymentRequest;
import com.microservicesteam.adele.payment.ExecutePaymentResponse;
import com.microservicesteam.adele.payment.ExecutionStatus;
import com.microservicesteam.adele.payment.PaymentManager;
import com.microservicesteam.adele.payment.PaymentRequest;
import com.microservicesteam.adele.payment.PaymentResponse;
import com.microservicesteam.adele.payment.PaymentStatus;
import com.microservicesteam.adele.payment.Ticket;
import com.microservicesteam.adele.ticketmaster.commands.CancelReservation;
import com.microservicesteam.adele.ticketmaster.commands.CloseReservation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderService extends EventBasedService {

    private static final String REDIRECT_PATH = "/orders/%s/payment?status=%s";
    private static final String APPROVED = "approved";
    private static final String CANCELLED = "cancelled";

    private final OrderConfiguration.OrderProperties orderProperties;
    private final OrderRepository orderRepository;
    private final PaymentManager paymentManager;
    private final Supplier<String> orderIdGenerator;
    private final Supplier<LocalDateTime> currentLocalDateTime;

    public OrderService(EventBus eventBus, OrderConfiguration.OrderProperties orderProperties,
            OrderRepository orderRepository, PaymentManager paymentManager, Supplier<String> orderIdGenerator,
            Supplier<LocalDateTime> currentLocalDateTime) {
        super(eventBus);
        this.orderProperties = orderProperties;
        this.orderRepository = orderRepository;
        this.paymentManager = paymentManager;
        this.orderIdGenerator = orderIdGenerator;
        this.currentLocalDateTime = currentLocalDateTime;
    }

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

    public void handlePayment(String orderId, String paymentId, String payerId, String status) {
        if (status.equals("success")) {
            int updatedRows = orderRepository.updateStatusByOrderIdPaymentIdStatus(OrderStatus.PAYMENT_APPROVED, orderId, paymentId, OrderStatus.PAYMENT_CREATED);
            if(updatedRows == 1 && executePayment(paymentId, payerId)) {
                orderRepository.updateStatusByOrderId(orderId, OrderStatus.PAID);
                // TODO fill the command with reservation data, when it will be available
                eventBus.post(CloseReservation.builder().build());
            }
        } else {
            orderRepository.updateStatusByOrderIdPaymentIdStatus(OrderStatus.PAYMENT_APPROVED, orderId, paymentId, OrderStatus.PAYMENT_CANCELLED);
            // TODO fill the command with reservation data, when it will be available
            eventBus.post(CancelReservation.builder().build());
        }
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

    private boolean executePayment(String paymentId, String payerId) {
        ExecutePaymentResponse executePaymentResponse = paymentManager.executePayment(ExecutePaymentRequest.builder()
                .paymentId(paymentId)
                .payerId(payerId)
                .build());
        return executePaymentResponse.status().equals(ExecutionStatus.APPROVED);
    }

}


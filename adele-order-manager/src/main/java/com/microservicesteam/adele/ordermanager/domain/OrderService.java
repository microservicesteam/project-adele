package com.microservicesteam.adele.ordermanager.domain;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.microservicesteam.adele.messaging.EventBasedService;
import com.microservicesteam.adele.ordermanager.domain.exception.InvalidPaymentResponseException;
import com.microservicesteam.adele.payment.PaymentManager;
import com.microservicesteam.adele.payment.PaymentRequest;
import com.microservicesteam.adele.payment.PaymentResponse;
import com.microservicesteam.adele.payment.PaymentStatus;
import com.microservicesteam.adele.payment.Ticket;
import com.microservicesteam.adele.ticketmaster.events.ReservationAccepted;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderService extends EventBasedService {

    private static final String URL = "/orders/%s/payment?status=%s";
    private static final String SUCCESS = "success";
    private static final String CANCELLED = "cancelled";

    private final OrderRepository orderRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentManager paymentManager;
    private final Supplier<String> orderIdGenerator;
    private final Supplier<LocalDateTime> currentLocalDateTime;

    public OrderService(OrderRepository orderRepository, ReservationRepository reservationRepository, PaymentManager paymentManager,
            Supplier<String> orderIdGenerator,
            Supplier<LocalDateTime> currentLocalDateTime, EventBus eventBus) {
        super(eventBus);
        this.orderRepository = orderRepository;
        this.reservationRepository = reservationRepository;
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

        String paymentID = paymentResponse.paymentId().orElseThrow(() -> new InvalidPaymentResponseException("Payment id missing to orderId: " + orderId));
        orderRepository.updatePaymentId(orderId, paymentID);

        String approveUrl = paymentResponse.approveUrl().orElseThrow(() -> new InvalidPaymentResponseException("Approve url is missing to orderId: " + orderId));
        return ApproveUrlResponse.builder()
                .approveUrl(approveUrl)
                .build();
    }

    @Subscribe
    public void handleEvent(ReservationAccepted reservationAccepted) {
        Reservation reservation = Reservation.builder()
                .reservationId(UUID.fromString(reservationAccepted.reservation().reservationId()))
                .build();

        reservation.setTicketIds(reservationAccepted.reservation().tickets().stream()
                .map(eventTicketId -> mapTicket(reservation, eventTicketId))
                .collect(toList()));
        reservationRepository.save(reservation);
    }

    private TicketId mapTicket(Reservation reservation, com.microservicesteam.adele.ticketmaster.model.TicketId eventTicketId) {
        return TicketId.builder()
                .programId(eventTicketId.programId())
                .sectorId(eventTicketId.sectorId())
                .seatId(eventTicketId.seatId())
                .reservation(reservation)
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
                .returnUrl(String.format(URL, orderId, SUCCESS))
                .cancelUrl(String.format(URL, orderId, CANCELLED))
                .build();
    }

}


package com.microservicesteam.adele.ordermanager.domain;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
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
import com.microservicesteam.adele.programmanager.boundary.web.ProgramRepository;
import com.microservicesteam.adele.programmanager.boundary.web.SectorRepository;
import com.microservicesteam.adele.programmanager.domain.Program;
import com.microservicesteam.adele.programmanager.domain.Sector;
import com.microservicesteam.adele.ticketmaster.events.ReservationAccepted;
import com.microservicesteam.adele.ticketmaster.model.Reservation;
import com.microservicesteam.adele.ticketmaster.model.TicketId;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderService extends EventBasedService {

    private static final String REDIRECT_PATH = "/orders/%s/payment?status=%s";
    private static final String APPROVED = "approved";
    private static final String CANCELLED = "cancelled";

    private final OrderConfiguration.OrderProperties orderProperties;
    private final OrderRepository orderRepository;
    private final ReservationRepository reservationRepository;
    private final ProgramRepository programRepository;
    private final SectorRepository sectorRepository;
    private final PaymentManager paymentManager;
    private final Supplier<String> orderIdGenerator;
    private final Supplier<LocalDateTime> currentLocalDateTime;

    public OrderService(OrderRepository orderRepository, ReservationRepository reservationRepository,
            ProgramRepository programRepository, SectorRepository sectorRepository,
            PaymentManager paymentManager, Supplier<String> orderIdGenerator, Supplier<LocalDateTime> currentLocalDateTime,
            EventBus eventBus, OrderConfiguration.OrderProperties orderProperties) {
        super(eventBus);
        this.orderRepository = orderRepository;
        this.reservationRepository = reservationRepository;
        this.programRepository = programRepository;
        this.sectorRepository = sectorRepository;
        this.paymentManager = paymentManager;
        this.orderIdGenerator = orderIdGenerator;
        this.currentLocalDateTime = currentLocalDateTime;
        this.orderProperties = orderProperties;
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

    @Subscribe
    public void handleEvent(ReservationAccepted reservationAccepted) {
        Reservation reservation = reservationAccepted.reservation();
        TicketId firstTicket = reservation.tickets().get(0);
        Program program = programRepository.findOne(firstTicket.programId());
        Sector sector = sectorRepository.findOne(Long.valueOf(firstTicket.sectorId()));
        reservation.tickets().forEach(ticketId -> {
            ReservedTicket reservedTicket = ReservedTicket.builder()
                    .reservationId(UUID.fromString(reservation.reservationId()))
                    .programId(firstTicket.programId())
                    .programName(program.name)
                    .programDescription(program.description)
                    .venueAddress(program.venue.address)
                    .price(sector.price.amount)
                    .currency(sector.price.currency)
                    .sector(firstTicket.sectorId())
                    .seat(ticketId.seatId())
                    .build();
            reservationRepository.save(reservedTicket);
        });
    }

    public void handlePayment(String orderId, String paymentId, String payerId, String status) {
        if (status.equals("success")) {
            int updatedRows = orderRepository.updateStatusByOrderIdPaymentIdStatus(OrderStatus.PAYMENT_CREATED, orderId, paymentId, OrderStatus.PAYMENT_APPROVED);
            if(updatedRows == 1) {
                ExecutePaymentResponse executePaymentResponse = executePayment(paymentId, payerId);
                if(ExecutionStatus.APPROVED.equals(executePaymentResponse.status())) {
                    orderRepository.updateStatusByOrderId(orderId, OrderStatus.PAID);
                    eventBus.post(CloseReservation.builder()
                            .reservation(getReservation(orderId))
                            .build());
                }
            }
        } else {
            orderRepository.updateStatusByOrderIdPaymentIdStatus(OrderStatus.PAYMENT_CREATED, orderId, paymentId, OrderStatus.PAYMENT_CANCELLED);
            eventBus.post(CancelReservation.builder()
                    .reservation(getReservation(orderId))
                    .build());
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
        Order order = orderRepository.findOne(orderId);
        List<ReservedTicket> reservedTickets =
                reservationRepository.findReservationsByReservationId(order.reservationId);

        if (reservedTickets.isEmpty()) {
            // Happens if ReservationAccepted event was not processed yet, but user already submitted create order page
            throw new IllegalStateException("Reserved tickets cannot be found for " + order.reservationId);
        }

        List<Ticket> tickets = reservedTickets.stream()
                .map(rt -> Ticket.builder()
                        .sector(rt.sector)
                        .priceAmount(rt.price)
                        .build())
                .collect(toList());

        ReservedTicket firstTicket = reservedTickets.get(0);
        return PaymentRequest.builder()
                .programName(firstTicket.programName)
                .programDescription(firstTicket.programDescription)
                .currency(firstTicket.currency)
                .tickets(tickets)
                .returnUrl(String.format(orderProperties.getDomainUrl() + REDIRECT_PATH, orderId, APPROVED))
                .cancelUrl(String.format(orderProperties.getDomainUrl() + REDIRECT_PATH, orderId, CANCELLED))
                .build();
    }

    private ExecutePaymentResponse executePayment(String paymentId, String payerId) {
        return paymentManager.executePayment(ExecutePaymentRequest.builder()
                .paymentId(paymentId)
                .payerId(payerId)
                .build());
    }

    private Reservation getReservation(String orderId) {
        Order order = orderRepository.findOne(orderId);
        List<ReservedTicket> reservedTickets = reservationRepository.findReservationsByReservationId(order.reservationId);
        return Reservation.builder()
                .reservationId(order.reservationId)
                .addAllTickets(reservedTickets.stream()
                        .map(reservedTicket -> TicketId.builder()
                                .programId(reservedTicket.programId)
                                .sectorId(reservedTicket.sector)
                                .seatId(reservedTicket.seat)
                                .build())
                        .collect(toList()))
                .build();
    }

}


package com.microservicesteam.adele.ordermanager.domain;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.microservicesteam.adele.messaging.EventBasedService;
import com.microservicesteam.adele.ordermanager.domain.ReservedTicket.ReservedTicketBuilder;
import com.microservicesteam.adele.ordermanager.domain.exception.InvalidPaymentResponseException;
import com.microservicesteam.adele.payment.PaymentManager;
import com.microservicesteam.adele.payment.PaymentRequest;
import com.microservicesteam.adele.payment.PaymentResponse;
import com.microservicesteam.adele.payment.PaymentStatus;
import com.microservicesteam.adele.payment.Ticket;
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

    private static final String URL = "/orders/%s/payment?status=%s";
    private static final String SUCCESS = "success";
    private static final String CANCELLED = "cancelled";

    private final OrderRepository orderRepository;
    private final ReservationRepository reservationRepository;
    private final ProgramRepository programRepository;
    private final SectorRepository sectorRepository;
    private final PaymentManager paymentManager;
    private final Supplier<String> orderIdGenerator;
    private final Supplier<LocalDateTime> currentLocalDateTime;

    public OrderService(OrderRepository orderRepository, ReservationRepository reservationRepository,
            ProgramRepository programRepository, SectorRepository sectorRepository,
            PaymentManager paymentManager, Supplier<String> orderIdGenerator, Supplier<LocalDateTime> currentLocalDateTime, EventBus eventBus) {
        super(eventBus);
        this.orderRepository = orderRepository;
        this.reservationRepository = reservationRepository;
        this.programRepository = programRepository;
        this.sectorRepository = sectorRepository;
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
        Reservation reservation = reservationAccepted.reservation();
        TicketId firstTicket = reservation.tickets().get(0);
        Program program = programRepository.findOne(firstTicket.programId());
        Sector sector = sectorRepository.findOne(Long.valueOf(firstTicket.sectorId()));
        ReservedTicketBuilder ticketBuilder = ReservedTicket.builder()
                .reservationId(UUID.fromString(reservation.reservationId()))
                .programId(firstTicket.programId())
                .programName(program.name)
                .programDescription(program.description)
                .venueAddress(program.venue.address)
                .price(sector.price.amount)
                .currency(sector.price.currency)
                .sectorId((long) firstTicket.sectorId())
                .sector(Math.toIntExact(sector.id)); //TODO fix this later to get real sector number
        reservation.tickets().forEach(ticketId -> {
            ReservedTicket reservedTicket = ticketBuilder
                    .seatId((long) ticketId.seatId())
                    .seat(ticketId.seatId()) //TODO fix this later to get the real seat number
                    .build();
            reservationRepository.save(reservedTicket);
        });
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
            throw new IllegalStateException("Reserved tickets cannot be found for " + order.reservationId);
        }
        ReservedTicket firstTicket = reservedTickets.get(0);

        List<Ticket> tickets = reservedTickets.stream()
                .map(rt -> Ticket.builder()
                        .sector(rt.sector)
                        .priceAmount(rt.price)
                        .build())
                .collect(toList());

        return PaymentRequest.builder()
                .programName(firstTicket.programName)
                .programDescription(firstTicket.programDescription)
                .currency(Currency.getInstance(firstTicket.currency))
                .tickets(tickets)
                .returnUrl(String.format(URL, orderId, SUCCESS))
                .cancelUrl(String.format(URL, orderId, CANCELLED))
                .build();
    }

}


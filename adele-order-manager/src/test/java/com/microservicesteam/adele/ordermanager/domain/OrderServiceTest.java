package com.microservicesteam.adele.ordermanager.domain;

import static com.microservicesteam.adele.programmanager.domain.ProgramStatus.OPEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.microservicesteam.adele.messaging.listeners.DeadEventListener;
import com.microservicesteam.adele.ordermanager.domain.exception.InvalidPaymentResponseException;
import com.microservicesteam.adele.payment.PaymentManager;
import com.microservicesteam.adele.payment.PaymentResponse;
import com.microservicesteam.adele.payment.PaymentStatus;
import com.microservicesteam.adele.programmanager.boundary.web.ProgramRepository;
import com.microservicesteam.adele.programmanager.boundary.web.SectorRepository;
import com.microservicesteam.adele.programmanager.domain.Price;
import com.microservicesteam.adele.programmanager.domain.Program;
import com.microservicesteam.adele.programmanager.domain.Sector;
import com.microservicesteam.adele.programmanager.domain.Venue;
import com.microservicesteam.adele.ticketmaster.events.ReservationAccepted;
import com.microservicesteam.adele.ticketmaster.model.Reservation;
import com.microservicesteam.adele.ticketmaster.model.TicketId;


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

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private SectorRepository sectorRepository;

    @Mock
    private OrderConfiguration.OrderProperties orderProperties;

    private OrderService orderService;

    private EventBus eventBus;
    private DeadEventListener deadEventListener;

    @Before
    public void setUp() {
        eventBus = new EventBus();
        deadEventListener = new DeadEventListener(eventBus);
        deadEventListener.init();
        orderService = new OrderService(orderRepository, reservationRepository, ticketIdRepository, programRepository, sectorRepository, paymentManager, idGenerator, currentLocalDateTime, eventBus, orderProperties);
        orderService.init();

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
    public void initiatePaymentShouldReturnApproveUrlWhenPaymentResponseIsCorrect() {
        PaymentResponse paymentResponse = PaymentResponse.builder()
                .status(PaymentStatus.CREATED)
                .paymentId(PAYMENT_ID)
                .approveUrl(APPROVE_URL)
                .build();
        ApproveUrlResponse expected = ApproveUrlResponse.builder()
                .approveUrl(APPROVE_URL)
                .build();
        Order order = givenOrder();
        when(paymentManager.initiatePayment(any())).thenReturn(paymentResponse);
        when(orderRepository.findOne(ORDER_ID)).thenReturn(order);
        when(reservationRepository.findReservationsByReservationId(order.reservationId))
                .thenReturn(ImmutableList.of(givenReservedTicket()));

        ApproveUrlResponse actual = orderService.initiatePayment(ORDER_ID);

        verify(paymentManager).initiatePayment(any());
        verify(orderRepository).updatePaymentId(ORDER_ID, PAYMENT_ID);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void initiatePaymentShouldThrowInvalidPaymentResponseExceptionWhenPaymentStatusIsFailed() {
        PaymentResponse paymentResponse = PaymentResponse.builder()
                .status(PaymentStatus.FAILED)
                .paymentId(PAYMENT_ID)
                .approveUrl(APPROVE_URL)
                .build();
        Order order = givenOrder();
        when(paymentManager.initiatePayment(any())).thenReturn(paymentResponse);
        when(orderRepository.findOne(ORDER_ID)).thenReturn(order);
        when(reservationRepository.findReservationsByReservationId(order.reservationId))
                .thenReturn(ImmutableList.of(givenReservedTicket()));

        Throwable actual = catchThrowable( () -> orderService.initiatePayment(ORDER_ID));

        verify(paymentManager).initiatePayment(any());
        assertThat(actual)
                .isInstanceOf(InvalidPaymentResponseException.class)
                .hasMessage(PAYMENT_INITIATION_FAILED_TO_ORDER_ID);
    }

    @Test
    public void initiatePaymentShouldThrowInvalidPaymentResponseExceptionWhenPaymentIdIsEmpty() {
        PaymentResponse paymentResponse = PaymentResponse.builder()
                .status(PaymentStatus.CREATED)
                .paymentId(Optional.empty())
                .approveUrl(APPROVE_URL)
                .build();
        Order order = givenOrder();
        when(paymentManager.initiatePayment(any())).thenReturn(paymentResponse);
        when(orderRepository.findOne(ORDER_ID)).thenReturn(order);
        when(reservationRepository.findReservationsByReservationId(order.reservationId))
                .thenReturn(ImmutableList.of(givenReservedTicket()));

        Throwable actual = catchThrowable( () -> orderService.initiatePayment(ORDER_ID));

        verify(paymentManager).initiatePayment(any());
        assertThat(actual)
                .isInstanceOf(InvalidPaymentResponseException.class)
                .hasMessage(PAYMENT_ID_MISSING_TO_ORDER_ID);
    }

    @Test
    public void initiatePaymentShouldThrowInvalidPaymentResponseExceptionWhenApproveUrlIsEmpty() {
        PaymentResponse paymentResponse = PaymentResponse.builder()
                .status(PaymentStatus.CREATED)
                .paymentId(PAYMENT_ID)
                .approveUrl(Optional.empty())
                .build();
        Order order = givenOrder();
        when(paymentManager.initiatePayment(any())).thenReturn(paymentResponse);
        when(orderRepository.findOne(ORDER_ID)).thenReturn(order);
        when(reservationRepository.findReservationsByReservationId(order.reservationId))
                .thenReturn(ImmutableList.of(givenReservedTicket()));
        Throwable actual = catchThrowable( () ->orderService.initiatePayment(ORDER_ID));

        verify(paymentManager).initiatePayment(any());
        assertThat(actual)
                .isInstanceOf(InvalidPaymentResponseException.class)
                .hasMessage(APPROVE_URL_IS_MISSING_TO_ORDER_ID);
    }

    @Test
    public void reservationAcceptedEventShouldBePersistedToReservationRepository() {
        TicketId ticket = TicketId.builder()
                .programId(1)
                .sectorId(2)
                .seatId(3)
                .build();
        ReservationAccepted reservationAccepted = ReservationAccepted.builder()
                .reservation(Reservation.builder()
                        .reservationId(RESERVATION_ID)
                        .addTickets(ticket)
                        .build())
                .build();
        Program program = Program.builder()
                .id(1L)
                .name("Program name")
                .description("Program description")
                .dateTime(LocalDateTime.of(2018, 4, 1, 15, 10))
                .venue(Venue.builder()
                        .address("Address")
                        .build())
                .status(OPEN)
                .build();
        when(programRepository.findOne(1L)).thenReturn(program);
        Sector sector = Sector.builder()
                .id(2L)
                .capacity(50)
                .price(Price.builder()
                        .amount(new BigDecimal("5000"))
                        .currency("HUF")
                        .build())
                .build();
        when(sectorRepository.findOne(2L)).thenReturn(sector);
        eventBus.post(reservationAccepted);

        ArgumentCaptor<ReservedTicket> captor = ArgumentCaptor.forClass(ReservedTicket.class);
        verify(reservationRepository).save(captor.capture());
        ReservedTicket capturedReservedTicket = captor.getValue();
        assertThat(capturedReservedTicket.reservationId.toString()).isEqualTo(RESERVATION_ID);
        assertThat(capturedReservedTicket.programId).isEqualTo(1);
        assertThat(capturedReservedTicket.programName).isEqualTo("Program name");
        assertThat(capturedReservedTicket.programDescription).isEqualTo("Program description");
        assertThat(capturedReservedTicket.venueAddress).isEqualTo("Address");
        assertThat(capturedReservedTicket.price).isEqualTo(new BigDecimal("5000"));
        assertThat(capturedReservedTicket.sectorId).isEqualTo(2);
        assertThat(capturedReservedTicket.sector).isEqualTo(2);
        assertThat(capturedReservedTicket.seat).isEqualTo(3);
        assertThat(capturedReservedTicket.seatId).isEqualTo(3);
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

    private ReservedTicket givenReservedTicket() {
        return ReservedTicket.builder()
                .id(1L)
                .programName("Awesome program")
                .programDescription("Awesome program description")
                .reservationId(UUID.fromString(RESERVATION_ID))
                .venueAddress("Venue address")
                .currency("HUF")
                .price(new BigDecimal("10"))
                .sector(2)
                .seat(3)
                .build();
    }

}
package com.microservicesteam.adele.model.data;

import static java.util.stream.Collectors.toList;
import static javax.persistence.CascadeType.PERSIST;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.microservicesteam.adele.model.Booking;
import com.microservicesteam.adele.model.PaymentStatus;

@Entity
public class BookingDo extends AbstractDo<Long> {

    public final String paymentId;

    public final PriceDo price;

    public final PaymentStatus status;
    
    @OneToMany(cascade = PERSIST)
    public final List<TicketDo> tickets;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public final UserDo user;

    private BookingDo() {
        super(null);
        this.paymentId = null;
        this.price = null;
        this.status = null;
        this.tickets = null;
        this.user = null;
    }

    private BookingDo(Long id, String paymentId, PriceDo price, PaymentStatus status, List<TicketDo> tickets, UserDo user) {
        super(id);
        this.paymentId = paymentId;
        this.price = price;
        this.status = status;
        this.tickets = tickets;
        this.user = user;
    }

    public Booking toImmutable() {
        return Booking.builder()
                .withId(id)
                .withPaymentId(paymentId)
                .withPrice(price.toImmutable())
                .withStatus(status)
                .addAllTickets(tickets.stream()
                        .map(ticket -> ticket.toImmutable())
                        .collect(toList()))
                .withUser(user.toImmutable())
                .build();
    }

    public static BookingDo fromImmutable(Booking booking) {
        return new BookingDo(booking.id(),
                booking.paymentId(),
                PriceDo.fromImmutable(booking.price()),
                booking.status(),
                booking.tickets().stream()
                        .map(ticket -> TicketDo.fromImmutable(ticket))
                        .collect(toList()),
                UserDo.fromImmutable(booking.user()));
    }

}

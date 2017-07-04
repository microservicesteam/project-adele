package com.microservicesteam.adele.booking.domain.data;

import static java.util.stream.Collectors.toList;
import static javax.persistence.CascadeType.PERSIST;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

import com.microservicesteam.adele.booking.domain.Booking;
import com.microservicesteam.adele.booking.domain.PaymentStatus;

@Entity
public class BookingDo extends AbstractDo<Long> {

    public final String paymentId;

    public final PriceDo sumPrice;

    public final PaymentStatus status;
    
    @OneToMany(cascade = PERSIST)
    public final List<TicketDo> tickets;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public final UserDo user;

    private BookingDo() {
        super(null);
        this.paymentId = null;
        this.sumPrice = null;
        this.status = null;
        this.tickets = null;
        this.user = null;
    }

    private BookingDo(Long id, String paymentId, PriceDo sumPrice, PaymentStatus status, List<TicketDo> tickets, UserDo user) {
        super(id);
        this.paymentId = paymentId;
        this.sumPrice = sumPrice;
        this.status = status;
        this.tickets = tickets;
        this.user = user;
    }

    public Booking toImmutable() {
        return Booking.builder()
                .id(id)
                .paymentId(paymentId)
                .sumPrice(sumPrice.toImmutable())
                .status(status)
                .addAllTickets(tickets.stream()
                        .map(TicketDo::toImmutable)
                        .collect(toList()))
                .user(user.toImmutable())
                .build();
    }

    public static BookingDo fromImmutable(Booking booking) {
        return new BookingDo(booking.id(),
                booking.paymentId(),
                PriceDo.fromImmutable(booking.sumPrice()),
                booking.status(),
                booking.tickets().stream()
                        .map(TicketDo::fromImmutable)
                        .collect(toList()),
                UserDo.fromImmutable(booking.user()));
    }

}

package com.microservicesteam.adele.booking.domain.data;


import com.microservicesteam.adele.booking.domain.Ticket;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import static javax.persistence.CascadeType.PERSIST;

@Entity
public class TicketDo extends AbstractDo<Long> {

    public final Long eventId;

    public final Integer position;

    public final PriceDo price;

    @OneToOne
    @JoinColumn(name = "visitor_id", nullable = false)
    public final VisitorDo visitor;
    
    @ManyToOne(cascade = PERSIST)
    @JoinColumn(name = "booking_id", nullable = false)
    public final BookingDo booking;

    private TicketDo() {
        super(null);
        this.eventId = null;
        this.position = null;
        this.price = null;
        this.visitor = null;
        this.booking = null;
    }

    private TicketDo(Long id, Long eventId, Integer position, PriceDo price, VisitorDo visitor) {
        super(id);
        this.eventId = eventId;
        this.position = position;
        this.price = price;
        this.visitor = visitor;
        this.booking = null;
    }

    public Ticket toImmutable() {
        return Ticket.builder()
                .withEventId(eventId)
                .withPosition(position)
                .withPrice(price.toImmutable())
                .withVisitor(visitor.toImmutable())
                .build();
    }

    public static TicketDo fromImmutable(Ticket ticket) {
        return new TicketDo(ticket.id(),
                ticket.eventId(),
                ticket.position(),
                PriceDo.fromImmutable(ticket.price()),
                VisitorDo.fromImmutable(ticket.visitor()));
    }

}

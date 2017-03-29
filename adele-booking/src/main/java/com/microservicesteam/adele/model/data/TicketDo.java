package com.microservicesteam.adele.model.data;

import com.microservicesteam.adele.model.Ticket;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import static javax.persistence.CascadeType.PERSIST;

@Entity
public class TicketDo extends AbstractDo<Long> {

    @ManyToOne(cascade = PERSIST)
    @JoinColumn(name = "event_id", nullable = false)
    public final EventDo event;

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
        this.event = null;
        this.position = null;
        this.price = null;
        this.visitor = null;
        this.booking = null;
    }

    private TicketDo(Long id, EventDo event, Integer position, PriceDo price, VisitorDo visitor) {
        super(id);
        this.event = event;
        this.position = position;
        this.price = price;
        this.visitor = visitor;
        this.booking = null;
    }

    public Ticket toImmutable() {
        return Ticket.builder()
                .withEvent(event.toImmutable())
                .withPosition(position)
                .withPrice(price.toImmutable())
                .withVisitor(visitor.toImmutable())
                .build();
    }

    public static TicketDo fromImmutable(Ticket ticket) {
        return new TicketDo(ticket.id(),
                EventDo.fromImmutable(ticket.event()),
                ticket.position(),
                PriceDo.fromImmutable(ticket.price()),
                VisitorDo.fromImmutable(ticket.visitor()));
    }

}

package com.microservicesteam.adele.model.data;

import static javax.persistence.CascadeType.PERSIST;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.microservicesteam.adele.model.Ticket;

@Entity
public class TicketDo extends AbstractDo<Long> {

    @ManyToOne(cascade = PERSIST)
    @JoinColumn(name = "event_id", nullable = false)
    public final EventDo event;

    @OneToOne
    @JoinColumn(name = "position_id", nullable = false)
    public final PositionDo position;

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

    private TicketDo(Long id, EventDo event, PositionDo position, PriceDo price, VisitorDo visitor) {
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
                .withPosition(position.toImmutable())
                .withPrice(price.toImmutable())
                .withVisitor(visitor.toImmutable())
                .build();
    }

    public static TicketDo fromImmutable(Ticket ticket) {
        return new TicketDo(ticket.id(),
                EventDo.fromImmutable(ticket.event()),
                PositionDo.fromImmutable(ticket.position()),
                PriceDo.fromImmutable(ticket.price()),
                VisitorDo.fromImmutable(ticket.visitor()));
    }

}

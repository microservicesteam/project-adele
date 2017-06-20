package com.microservicesteam.adele.event.domain.data;

import com.microservicesteam.adele.event.domain.Event;
import com.microservicesteam.adele.event.domain.EventStatus;

import static javax.persistence.CascadeType.PERSIST;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class EventDo extends AbstractDo<Long> {

    public final String name;

    public final String description;

    public final EventStatus status;

    public final LocalDateTime dateTime;
    
    @OneToOne(cascade = PERSIST)
    public final VenueDo venue;

    private EventDo() {
        super(null);
        this.name = null;
        this.description = null;
        this.status = null;
        this.dateTime = null;
        this.venue = null;
    }

    private EventDo(Long id, String name, String description, EventStatus status, LocalDateTime dateTime, VenueDo venue) {
        super(id);
        this.name = name;
        this.description = description;
        this.status = status;
        this.dateTime = dateTime;
        this.venue = venue;
    }
    
    public Event toImmutable() {
        return Event.builder()
                .id(id)
                .name(name)
                .description(description)
                .status(status)
                .dateTime(dateTime)
                .venue(venue.toImmutable())
                .build();
    }
    
    public static EventDo fromImmutable(Event event) {
        return new EventDo(event.id(), event.name(), event.description(), event.status(), event.dateTime(), VenueDo.fromImmutable(event.venue()));
    }
}

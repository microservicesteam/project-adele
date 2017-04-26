package com.microservicesteam.adele.service;

import com.microservicesteam.adele.messaging.events.Event;
import com.microservicesteam.adele.ticketmaster.events.TicketsBooked;
import com.microservicesteam.adele.ticketmaster.model.Position;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class BookingScheduler {

    private AtomicBoolean enabled = new AtomicBoolean(false);

    private AtomicInteger counter = new AtomicInteger(0);

    private BookingPublisher bookingPublisher;

    public BookingScheduler(BookingPublisher bookingPublisher) {
        this.bookingPublisher = bookingPublisher;
    }

    public void start() {
        enabled.set(true);
    }

    public void stop() {
        enabled.set(false);
    }

    @Scheduled(fixedRate = 2000)
    public void invokePublisher() {
        Event event = randomEvent();
        bookingPublisher.publish(event);
    }

    private Event randomEvent() {
        Position position = Position.builder()
                .id(new Random().nextInt(100))
                .sectorId(1)
                .build();
        TicketsBooked ticketsBooked = TicketsBooked.builder()
                .eventId(1L)
                .bookingId(counter.getAndIncrement())
                .addPositions(position)
                .build();

        return ticketsBooked;
    }
}

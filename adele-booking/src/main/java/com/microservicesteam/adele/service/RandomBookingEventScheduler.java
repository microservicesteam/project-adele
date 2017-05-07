package com.microservicesteam.adele.service;

import com.microservicesteam.adele.boundary.web.BookingPublisher;
import com.microservicesteam.adele.messaging.events.Event;
import com.microservicesteam.adele.ticketmaster.events.TicketsBooked;
import com.microservicesteam.adele.ticketmaster.model.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/*!!! For testing purposes only !!!*/
@Component
public class RandomBookingEventScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RandomBookingEventScheduler.class);

    private AtomicBoolean enabled = new AtomicBoolean(false);

    private AtomicInteger counter = new AtomicInteger(0);

    private BookingPublisher bookingPublisher;

    public RandomBookingEventScheduler(BookingPublisher bookingPublisher) {
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
        if (enabled.get()) {
            Event event = randomEvent();
            LOGGER.info("Publishing random event: {}", event);
            bookingPublisher.publish(event);
        }
    }

    private Event randomEvent() {
        Position position = Position.builder()
                .id(new Random().nextInt(100))
                .eventId(1)
                .sectorId(1)
                .build();

        return TicketsBooked.builder()
                .bookingId(String.valueOf(counter.getAndIncrement()))
                .addPositions(position)
                .build();
    }
}

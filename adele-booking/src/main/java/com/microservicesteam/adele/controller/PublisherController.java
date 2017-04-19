package com.microservicesteam.adele.controller;

import com.microservicesteam.adele.service.BookingPublisher;
import com.microservicesteam.adele.ticketmaster.events.TicketsBooked;
import com.microservicesteam.adele.ticketmaster.model.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class PublisherController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublisherController.class);

    private AtomicInteger counter;

    private BookingPublisher bookingPublisher;

    @Autowired
    public PublisherController(BookingPublisher bookingPublisher) {
        this.bookingPublisher = bookingPublisher;
        this.counter = new AtomicInteger(0);
    }


    @RequestMapping(path="/triggerTicketsBooked", method = GET)
    public String ticketsBooked(@RequestParam(value = "position") int positionId) {
        Position position = Position.builder()
                .id(positionId)
                .sectorId(1)
                .build();
        TicketsBooked ticketsBooked = TicketsBooked.builder()
                .eventId(1L)
                .bookingId(counter.getAndIncrement())
                .addPositions(position)
                .build();

        LOGGER.info("Publishing event {}", ticketsBooked);
        bookingPublisher.publish(ticketsBooked);

        return "OK";
    }
}

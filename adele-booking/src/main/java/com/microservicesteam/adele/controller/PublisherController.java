package com.microservicesteam.adele.controller;

import com.microservicesteam.adele.service.RandomBookingEventScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/*!!! For testing purposes only !!!*/

@RestController
@RequestMapping("/rs/api")
public class PublisherController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublisherController.class);

    private RandomBookingEventScheduler bookingScheduler;

    public PublisherController(RandomBookingEventScheduler bookingScheduler) {
        this.bookingScheduler = bookingScheduler;
    }

    @RequestMapping(path="/triggerTicketsBooked/start", method = POST)
    public void startBookingScheduler() {
        LOGGER.info("Starting scheduler");
        bookingScheduler.start();
    }

    @RequestMapping(path="/triggerTicketsBooked/stop", method = POST)
    public void stopBookingScheduler() {
        LOGGER.info("Stopping scheduler");
        bookingScheduler.stop();
    }
}

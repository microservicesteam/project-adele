package com.microservicesteam.adele.booking.boundary.web;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservicesteam.adele.booking.TestRandomBookingEventScheduler;

/*!!! For testing purposes only !!!*/

@RestController
@RequestMapping("/rs/api")
public class TestPublisherController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestPublisherController.class);

    private TestRandomBookingEventScheduler bookingScheduler;

    public TestPublisherController(TestRandomBookingEventScheduler bookingScheduler) {
        this.bookingScheduler = bookingScheduler;
    }

    @RequestMapping(path = "/triggerTicketsBooked/start", method = POST)
    public void startBookingScheduler() {
        LOGGER.info("Starting scheduler");
        bookingScheduler.start();
    }

    @RequestMapping(path = "/triggerTicketsBooked/stop", method = POST)
    public void stopBookingScheduler() {
        LOGGER.info("Stopping scheduler");
        bookingScheduler.stop();
    }
}

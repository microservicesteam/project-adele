package com.microservicesteam.adele.booking;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservicesteam.adele.model.Event;
import com.microservicesteam.adele.services.BookingService;

@RestController
@RequestMapping("/rs/api")
public class BookingController {

	public static final Logger LOGGER = LoggerFactory.getLogger(BookingController.class);
	
	@Autowired
	private BookingService bookingService;
	
	@RequestMapping(value = "/events", method = GET)
	public List<Event> getEvents() {
		LOGGER.debug("Requested get events");
		return bookingService.getEvents();
	}
	
}

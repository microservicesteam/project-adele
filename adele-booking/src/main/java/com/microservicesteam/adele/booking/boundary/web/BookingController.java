package com.microservicesteam.adele.booking.boundary.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservicesteam.adele.booking.domain.BookingRequest;
import com.microservicesteam.adele.booking.domain.BookingResponse;
import com.microservicesteam.adele.booking.domain.BookingService;
import com.microservicesteam.adele.ticketmaster.model.Ticket;

@RestController
@RequestMapping("bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<Ticket> getBookings(@RequestParam long eventId) {
        return bookingService.getTicketsStatus(eventId);
    }

    @PostMapping
    public BookingResponse bookTickets(@RequestBody BookingRequest bookingRequest) {
        return bookingService.bookTickets(bookingRequest);
    }

}

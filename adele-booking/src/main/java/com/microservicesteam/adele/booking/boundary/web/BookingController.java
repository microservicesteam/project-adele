package com.microservicesteam.adele.booking.boundary.web;

import com.microservicesteam.adele.booking.domain.BookingRequest;
import com.microservicesteam.adele.booking.domain.BookingResponse;
import com.microservicesteam.adele.booking.domain.BookingService;
import com.microservicesteam.adele.ticketmaster.model.Ticket;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

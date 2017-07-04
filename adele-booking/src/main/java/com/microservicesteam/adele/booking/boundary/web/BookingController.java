package com.microservicesteam.adele.booking.boundary.web;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.microservicesteam.adele.booking.domain.BookingRequest;
import com.microservicesteam.adele.booking.domain.BookingResponse;
import com.microservicesteam.adele.booking.domain.BookingService;
import com.microservicesteam.adele.ticketmaster.model.Ticket;

@RestController
@RequestMapping("/events/{eventId}")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/tickets")
    public List<Ticket> getTicketsStatus(@PathVariable long eventId) {
        return bookingService.getTicketsStatus();
    }

    @PostMapping("/book-tickets")
    public BookingResponse bookTickets(@PathVariable long eventId, @RequestBody BookingRequest bookingRequest) {
        return bookingService.bookTickets(bookingRequest);
    }

}

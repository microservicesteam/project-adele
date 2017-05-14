package com.microservicesteam.adele.booking.boundary.web;

import com.microservicesteam.adele.booking.domain.BookingRequest;
import com.microservicesteam.adele.booking.domain.BookingResponse;
import com.microservicesteam.adele.booking.domain.BookingService;
import com.microservicesteam.adele.ticketmaster.model.Ticket;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public BookingResponse bookTickets(@RequestBody BookingRequest bookingRequest) {
        return bookingService.bookTickets(bookingRequest);
    }

}

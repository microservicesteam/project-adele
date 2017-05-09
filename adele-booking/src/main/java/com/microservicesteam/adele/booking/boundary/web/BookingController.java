package com.microservicesteam.adele.booking.boundary.web;

import com.microservicesteam.adele.booking.domain.BookingRequest;
import com.microservicesteam.adele.booking.domain.BookingResponse;
import com.microservicesteam.adele.booking.domain.BookingService;
import com.microservicesteam.adele.ticketmaster.model.Ticket;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/rs/api")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/mapStatus")
    public List<Ticket> getMapStatus() {
        return bookingService.getTicketsStatus().stream()
                .sorted(comparingInt(t -> t.position().id()))
                .collect(toList());
    }

    @PostMapping("/bookTickets")
    public BookingResponse bookTickets(@RequestBody BookingRequest bookingRequest) {
        return bookingService.bookTickets(bookingRequest);
    }

}

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
@RequestMapping("tickets")
public class TicketController {

    private final BookingService bookingService;

    public TicketController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping(params = {"eventId"})
    public List<Ticket> getTickets(@RequestParam long eventId) {
        return bookingService.getTicketsStatusByEvent(eventId);
    }

    @GetMapping(params = {"eventId", "sectorId"})
    public List<Ticket> getTickets(@RequestParam long eventId, @RequestParam int sectorId) {
        return bookingService.getTicketsStatusByEventAndSector(eventId, sectorId);
    }

}

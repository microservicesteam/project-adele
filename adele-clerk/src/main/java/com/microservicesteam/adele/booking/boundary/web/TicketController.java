package com.microservicesteam.adele.booking.boundary.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservicesteam.adele.booking.domain.TicketsService;
import com.microservicesteam.adele.ticketmaster.model.Ticket;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("tickets")
@AllArgsConstructor
public class TicketController {

    private final TicketsService ticketsService;

    @GetMapping(params = {"eventId"})
    public List<Ticket> getTickets(@RequestParam long eventId) {
        return ticketsService.getTicketsStatusByEvent(eventId);
    }

    @GetMapping(params = {"eventId", "sectorId"})
    public List<Ticket> getTickets(@RequestParam long eventId, @RequestParam int sectorId) {
        return ticketsService.getTicketsStatusByEventAndSector(eventId, sectorId);
    }

}

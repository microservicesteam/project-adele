package com.microservicesteam.adele.clerk.boundary.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservicesteam.adele.clerk.domain.TicketsService;
import com.microservicesteam.adele.ticketmaster.model.Ticket;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("tickets")
@AllArgsConstructor
public class TicketController {

    private final TicketsService ticketsService;

    @GetMapping(params = {"programId"})
    public List<Ticket> getTickets(@RequestParam long programId) {
        return ticketsService.getTicketsStatusByProgram(programId);
    }

    @GetMapping(params = {"programId", "sectorId"})
    public List<Ticket> getTickets(@RequestParam long programId, @RequestParam int sectorId) {
        return ticketsService.getTicketsStatusByProgramAndSector(programId, sectorId);
    }

}

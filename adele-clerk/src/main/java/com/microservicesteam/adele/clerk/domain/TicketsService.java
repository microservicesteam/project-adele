package com.microservicesteam.adele.clerk.domain;

import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.microservicesteam.adele.messaging.EventBasedService;
import com.microservicesteam.adele.ticketmaster.events.TicketsCreated;
import com.microservicesteam.adele.ticketmaster.model.Ticket;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TicketsService extends EventBasedService {
    private final TicketRepository ticketRepository;

    public TicketsService(EventBus eventBus,
            TicketRepository ticketRepository) {
        super(eventBus);
        this.ticketRepository = ticketRepository;
    }

    public ImmutableList<Ticket> getTicketsStatusByProgram(long programId) {
        return ticketRepository.getTicketsStatusByProgram(programId);
    }

    public ImmutableList<Ticket> getTicketsStatusByProgramAndSector(long programId, int sector) {
        return ticketRepository.getTicketsStatusByProgramAndSector(programId, sector);
    }

    @Subscribe
    public void handleEvent(TicketsCreated ticketsCreated) {
        ticketsCreated.tickets()
                .forEach(ticketRepository::put);
    }
}

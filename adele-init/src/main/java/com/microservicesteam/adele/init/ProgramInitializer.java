package com.microservicesteam.adele.init;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.microservicesteam.adele.programmanager.boundary.web.ProgramRepository;
import com.microservicesteam.adele.programmanager.domain.Coordinates;
import com.microservicesteam.adele.programmanager.domain.Price;
import com.microservicesteam.adele.programmanager.domain.Program;
import com.microservicesteam.adele.programmanager.domain.ProgramStatus;
import com.microservicesteam.adele.programmanager.domain.Sector;
import com.microservicesteam.adele.programmanager.domain.Venue;
import com.microservicesteam.adele.ticketmaster.commands.CreateTickets;
import com.microservicesteam.adele.ticketmaster.model.Ticket;
import com.microservicesteam.adele.ticketmaster.model.TicketId;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Component
public class ProgramInitializer implements CommandLineRunner {

    private static final int PROGRAM_ID = 1;
    private static final int NUMBER_OF_SECTORS = 50;
    private static final int SECTOR_CAPACITY = 250;

    private final ProgramRepository programRepository;
    private final EventBus eventBus;

    @Override
    public void run(String... args) {
        initProgramRepository();
        emitTicketCreatedEvents();
    }

    private void initProgramRepository() {
        log.info("Initializing program repository with data...");
        Program program1 = Program.builder()
                .name("Init test program")
                .description("Lorem ipsum dolor met")
                .status(ProgramStatus.OPEN)
                .dateTime(LocalDateTime.now())
                .venue(Venue.builder()
                        .address("Test venue address")
                        .coordinates(Coordinates.builder()
                                .latitude(1.0)
                                .longitude(2.1)
                                .build())
                        .sectors(createSectors())
                        .build())
                .build();
        Program program2 = Program.builder()
                .name("Another program")
                .description("Closed program description")
                .status(ProgramStatus.CLOSED)
                .dateTime(LocalDateTime.now())
                .venue(Venue.builder()
                        .address("Another test venue address")
                        .coordinates(Coordinates.builder()
                                .latitude(3.0)
                                .longitude(1.1)
                                .build())
                        .build())
                .build();
        programRepository.save(program1);
        programRepository.save(program2);
    }

    private void emitTicketCreatedEvents() {
        log.info("Creating new tickets for TicketMaster...");

        CreateTickets.Builder builder = CreateTickets.builder();
        IntStream.rangeClosed(1, NUMBER_OF_SECTORS)
                .forEach(sectorId -> builder.addAllTickets(createTickets(sectorId)));

        eventBus.post(builder.build());
    }

    private static List<Sector> createSectors() {
        return IntStream.rangeClosed(1, NUMBER_OF_SECTORS)
                .mapToObj(value -> createSector())
                .collect(toList());
    }

    private static Sector createSector() {
        List<Integer> seats = IntStream.rangeClosed(1, SECTOR_CAPACITY)
                .boxed()
                .collect(toList());
        return Sector.builder()
                .capacity(SECTOR_CAPACITY)
                .price(Price.builder()
                        .currency("HUF")
                        .amount(new BigDecimal("1500"))
                        .build())
                .seats(seats)
                .build();
    }

    private static List<Ticket> createTickets(int sectorId) {
        return IntStream.rangeClosed(1, SECTOR_CAPACITY)
                .mapToObj(id -> Ticket.builder()
                        .ticketId(createTicket(PROGRAM_ID, sectorId, id))
                        .status(FREE)
                        .build())
                .collect(toImmutableList());
    }

    private static TicketId createTicket(int programId, int sectorId, int seatId) {
        return TicketId.builder()
                .programId(programId)
                .sectorId(sectorId)
                .seatId(seatId)
                .build();
    }

}

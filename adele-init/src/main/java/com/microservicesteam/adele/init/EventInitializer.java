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
import com.microservicesteam.adele.admin.boundary.web.EventRepository;
import com.microservicesteam.adele.admin.domain.Coordinates;
import com.microservicesteam.adele.admin.domain.Event;
import com.microservicesteam.adele.admin.domain.EventStatus;
import com.microservicesteam.adele.admin.domain.Price;
import com.microservicesteam.adele.admin.domain.Sector;
import com.microservicesteam.adele.admin.domain.Venue;
import com.microservicesteam.adele.ticketmaster.commands.CreateTickets;
import com.microservicesteam.adele.ticketmaster.model.Ticket;
import com.microservicesteam.adele.ticketmaster.model.TicketId;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Component
public class EventInitializer implements CommandLineRunner {

    private static final int EVENT_ID = 1;
    private static final int NUMBER_OF_SECTORS = 50;
    private static final int SECTOR_CAPACITY = 250;

    private final EventRepository eventRepository;
    private final EventBus eventBus;

    @Override
    public void run(String... args) {
        initEventRepository();
        emitTicketCreatedEvents();
    }

    private void initEventRepository() {
        log.info("Initializing event repository with data...");
        Event event1 = Event.builder()
                .name("Init test event")
                .description("Lorem ipsum dolor met")
                .status(EventStatus.OPEN)
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
        Event event2 = Event.builder()
                .name("Another event")
                .description("Closed event description")
                .status(EventStatus.CLOSED)
                .dateTime(LocalDateTime.now())
                .venue(Venue.builder()
                        .address("Another test venue address")
                        .coordinates(Coordinates.builder()
                                .latitude(3.0)
                                .longitude(1.1)
                                .build())
                        .build())
                .build();
        eventRepository.save(event1);
        eventRepository.save(event2);
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
                        .ticketId(createTicket(EVENT_ID, sectorId, id))
                        .status(FREE)
                        .build())
                .collect(toImmutableList());
    }

    private static TicketId createTicket(int eventId, int sectorId, int seatId) {
        return TicketId.builder()
                .eventId(eventId)
                .sectorId(sectorId)
                .seatId(seatId)
                .build();
    }

}

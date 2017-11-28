package com.microservicesteam.adele.init;

import static com.google.common.collect.ImmutableList.toImmutableList;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.microservicesteam.adele.event.boundary.web.EventRepository;
import com.microservicesteam.adele.event.domain.Coordinates;
import com.microservicesteam.adele.event.domain.Event;
import com.microservicesteam.adele.event.domain.EventStatus;
import com.microservicesteam.adele.event.domain.Price;
import com.microservicesteam.adele.event.domain.Sector;
import com.microservicesteam.adele.event.domain.Venue;
import com.microservicesteam.adele.event.domain.data.EventDo;
import com.microservicesteam.adele.ticketmaster.commands.CreateTickets;
import com.microservicesteam.adele.ticketmaster.model.Position;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EventInitializer implements CommandLineRunner {

    private final EventRepository eventRepository;

    private final EventBus eventBus;

    public EventInitializer(EventRepository eventRepository, EventBus eventBus) {
        this.eventRepository = eventRepository;
        this.eventBus = eventBus;
    }


    @Override
    public void run(String... arg0) throws Exception {
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
                        .addSectors(Sector.builder()
                                .capacity(5)
                                .price(Price.builder()
                                        .currency("HUF")
                                        .amount(new BigDecimal("1500"))
                                        .build())
                                .addPositions(1, 2, 3, 4, 5)
                                .build(),
                                Sector.builder()
                                .capacity(6)
                                .price(Price.builder()
                                        .currency("HUF")
                                        .amount(new BigDecimal("2000"))
                                        .build())
                                .addPositions(1, 2, 3, 4, 5, 6)
                                .build())
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
        eventRepository.save(EventDo.fromImmutable(event1));
        eventRepository.save(EventDo.fromImmutable(event2));
    }

    private void emitTicketCreatedEvents() {
        log.info("Creating new tickets for TicketMaster...");
        CreateTickets createTicketsCommandForSector1 = CreateTickets.builder()
                .addAllPositions(IntStream.rangeClosed(1, 5)
                        .mapToObj(id -> createPositionWithSectorAndId(1, id))
                        .collect(toImmutableList()))
                .build();
        CreateTickets createTicketsCommandForSector2 = CreateTickets.builder()
                .addAllPositions(IntStream.rangeClosed(1, 6)
                        .mapToObj(id -> createPositionWithSectorAndId(2, id))
                        .collect(toImmutableList()))
                .build();
        eventBus.post(createTicketsCommandForSector1);
        eventBus.post(createTicketsCommandForSector2);
    }

    private static Position createPositionWithSectorAndId(int sector, int id) {
        return Position.builder()
                .eventId(1)
                .sectorId(sector)
                .id(id)
                .build();
    }

}

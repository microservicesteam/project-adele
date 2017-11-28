package com.microservicesteam.adele.init;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
import com.microservicesteam.adele.ticketmaster.commands.CreateTickets;
import com.microservicesteam.adele.ticketmaster.model.Position;
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
                .forEach(sectorId -> builder.addAllPositions(createPositions(sectorId)));

        eventBus.post(builder.build());
    }

    private static List<Sector> createSectors() {
        return IntStream.rangeClosed(1, NUMBER_OF_SECTORS)
                .mapToObj(value -> createSector())
                .collect(toList());
    }

    private static Sector createSector() {
        List<Integer> positions = IntStream.rangeClosed(1, SECTOR_CAPACITY)
                .mapToObj(Integer::new)
                .collect(toList());
        return Sector.builder()
                .capacity(SECTOR_CAPACITY)
                .price(Price.builder()
                        .currency("HUF")
                        .amount(new BigDecimal("1500"))
                        .build())
                .positions(positions)
                .build();
    }

    private static List<Position> createPositions(int sectorId) {
        return IntStream.rangeClosed(1, SECTOR_CAPACITY)
                .mapToObj(id -> createPositionWithId(EVENT_ID, sectorId, id))
                .collect(toImmutableList());
    }

    private static Position createPositionWithId(int eventId, int sectorId, int id) {
        return Position.builder()
                .eventId(eventId)
                .sectorId(sectorId)
                .id(id)
                .build();
    }

}

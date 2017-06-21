package com.microservicesteam.adele.init;

import static com.google.common.collect.ImmutableList.toImmutableList;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.microservicesteam.adele.event.boundary.web.EventRepository;
import com.microservicesteam.adele.event.domain.*;
import com.microservicesteam.adele.event.domain.data.EventDo;
import com.microservicesteam.adele.ticketmaster.commands.CreateTickets;
import com.microservicesteam.adele.ticketmaster.model.Position;

@Component
public class EventInitializer implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventInitializer.class);

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
        LOGGER.info("Initializing event repository with data...");
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
        LOGGER.info("Creating new tickets for TicketMaster...");
        CreateTickets createTicketsCommand = CreateTickets.builder()
                .addAllPositions(IntStream.rangeClosed(1, 5)
                        .mapToObj(EventInitializer::createPositionWithId)
                        .collect(toImmutableList()))
                .build();
        eventBus.post(createTicketsCommand);
    }

    private static Position createPositionWithId(int id) {
        return Position.builder()
                .eventId(1)
                .sectorId(1)
                .id(id)
                .build();
    }

}

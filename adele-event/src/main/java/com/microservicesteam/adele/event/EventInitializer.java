package com.microservicesteam.adele.event;

import static com.microservicesteam.adele.event.domain.EventStatus.CLOSED;
import static com.microservicesteam.adele.event.domain.EventStatus.OPEN;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.microservicesteam.adele.event.boundary.web.EventRepository;
import com.microservicesteam.adele.event.domain.*;
import com.microservicesteam.adele.event.domain.data.EventDo;

@Component
public class EventInitializer implements CommandLineRunner {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public void run(String... arg0) throws Exception {
        Event event1 = Event.builder()
                .withName("Init test event")
                .withDescription("Lorem ipsum dolor met")
                .withStatus(OPEN)
                .withDateTime(LocalDateTime.now())
                .withVenue(Venue.builder()
                        .withAddress("Test venue address")
                        .withCoordinates(Coordinates.builder()
                                .withLatitude(1.0)
                                .withLongitude(2.1)
                                .build())
                        .addSectors(Sector.builder()
                                .withCapacity(2)
                                .withPrice(Price.builder()
                                        .withCurrency("HUF")
                                        .withAmount(new BigDecimal("1500"))
                                        .build())
                                .addPositions(1)
                                .addPositions(2)
                                .build())
                        .build())
                .build();
        Event event2 = Event.builder()
                .withName("Another event")
                .withDescription("Closed event description")
                .withStatus(CLOSED)
                .withDateTime(LocalDateTime.now())
                .withVenue(Venue.builder()
                        .withAddress("Another test venue address")
                        .withCoordinates(Coordinates.builder()
                                .withLatitude(3.0)
                                .withLongitude(1.1)
                                .build())
                        .build())
                .build();
        eventRepository.save(EventDo.fromImmutable(event1));
        eventRepository.save(EventDo.fromImmutable(event2));
    }

}

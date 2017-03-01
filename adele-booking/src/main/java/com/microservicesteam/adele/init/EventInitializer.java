package com.microservicesteam.adele.init;

import static com.microservicesteam.adele.model.EventStatus.CLOSED;
import static com.microservicesteam.adele.model.EventStatus.OPEN;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.microservicesteam.adele.model.Coordinates;
import com.microservicesteam.adele.model.Event;
import com.microservicesteam.adele.model.Venue;
import com.microservicesteam.adele.model.data.EventDo;
import com.microservicesteam.adele.model.data.VenueDo;
import com.microservicesteam.adele.repositories.EventRepository;

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
                        .build())
                .build();
        eventRepository.save(EventDo.fromImmutable(event1));
        //eventRepository.save(new EventDo("Second event", "Closed event description", CLOSED, LocalDateTime.now(), new VenueDo("Test venue address2")));
    }

}

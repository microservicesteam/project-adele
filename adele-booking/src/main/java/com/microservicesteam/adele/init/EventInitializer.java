package com.microservicesteam.adele.init;

import static com.microservicesteam.adele.model.EventStatus.CLOSED;
import static com.microservicesteam.adele.model.EventStatus.OPEN;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.microservicesteam.adele.model.data.EventDo;
import com.microservicesteam.adele.model.data.VenueDo;
import com.microservicesteam.adele.repositories.EventRepository;

@Component
public class EventInitializer implements CommandLineRunner {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public void run(String... arg0) throws Exception {
        eventRepository.save(new EventDo("Init event", "Init event description", OPEN, LocalDateTime.now(), new VenueDo("Test venue address")));
        eventRepository.save(new EventDo("Second event", "Closed event description", CLOSED, LocalDateTime.now(), new VenueDo("Test venue address2")));
    }

}

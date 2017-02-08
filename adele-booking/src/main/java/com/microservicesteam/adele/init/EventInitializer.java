package com.microservicesteam.adele.init;

import static com.microservicesteam.adele.model.EventStatus.OPEN;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.microservicesteam.adele.model.data.EventDo;
import com.microservicesteam.adele.repositories.EventRepository;

import java.time.LocalDateTime;

@Component
public class EventInitializer implements CommandLineRunner {

	@Autowired
	private EventRepository eventRepository;
	
	@Override
	public void run(String... arg0) throws Exception {
		eventRepository.save(new EventDo("Init event", "Init event description", OPEN, LocalDateTime.now()));
		
	}

}

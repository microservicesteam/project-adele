package com.microserviceteam.adele.ticketmaster.events;

import org.junit.Before;
import org.springframework.boot.test.json.JacksonTester;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.microservicesteam.adele.ticketmaster.events.Event;

public class AbstractSerializationTest {

    JacksonTester<Event> json;

    @Before
    public void setUp() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new GuavaModule());
        JacksonTester.initFields(this, objectMapper);
    }

}

package com.microservicesteam.adele.messaging.publishers;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.eventbus.EventBus;
import com.microservicesteam.adele.messaging.events.Event;

@RunWith(MockitoJUnitRunner.class)
public class EventPublisherTest {

    @Mock
    private Event event;
    @Mock
    private EventBus eventBus;

    private EventPublisher eventPublisher;

    @Before
    public void setUp() {
        eventPublisher = new EventPublisher(eventBus);
    }

    @Test
    public void publishEvent() {
        eventPublisher.publish(event);

        verify(eventBus).post(event);
    }
}
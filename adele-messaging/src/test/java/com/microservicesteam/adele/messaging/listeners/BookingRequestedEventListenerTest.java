package com.microservicesteam.adele.messaging.listeners;

import static org.mockito.Mockito.verify;

import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.eventbus.EventBus;
import com.microservicesteam.adele.messaging.events.BookingRequestedEvent;

@RunWith(MockitoJUnitRunner.class)
public class BookingRequestedEventListenerTest {

    private EventBus eventBus;

    @Mock
    private BookingRequestedEvent BOOKING_REQUESTED_EVENT;
    @Mock
    private Consumer<BookingRequestedEvent> consumer;

    @Before
    public void setUp() throws Exception {
        eventBus = new EventBus();
        EventListener<BookingRequestedEvent> eventListener = new BookingRequestedEventListener(eventBus);
        eventListener.addConsumer(consumer);
        eventBus.register(eventListener);
    }

    @Test
    public void receiveBookingCancelledEvent() throws Exception {
        eventBus.post(BOOKING_REQUESTED_EVENT);

        verify(consumer).accept(BOOKING_REQUESTED_EVENT);
    }
}
package com.microservicesteam.adele.messaging.listeners;

import static org.mockito.Mockito.verify;

import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.eventbus.EventBus;
import com.microservicesteam.adele.messaging.events.BookingCancelledEvent;

@RunWith(MockitoJUnitRunner.class)
public class BookingCancelledEventListenerTest {

    private EventBus eventBus;

    @Mock
    private BookingCancelledEvent BOOKING_CANCELLED_EVENT;

    @Mock
    private Consumer<BookingCancelledEvent> consumer;

    @Before
    public void setUp() throws Exception {
        eventBus = new EventBus();
        EventListener<BookingCancelledEvent> eventListener = new BookingCancelledEventListener(eventBus);
        eventListener.addConsumer(consumer);
        eventBus.register(eventListener);
    }

    @Test
    public void receiveBookingCancelledEvent() throws Exception {
        eventBus.post(BOOKING_CANCELLED_EVENT);

        verify(consumer).accept(BOOKING_CANCELLED_EVENT);
    }
}
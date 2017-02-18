package com.microservicesteam.adele.messaging.listeners;

import static com.microservicesteam.adele.messaging.events.EventType.BOOKING_CANCELLED;
import static org.assertj.core.api.Assertions.assertThat;

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
    private String result = "";

    @Mock
    private BookingCancelledEvent BOOKING_CANCELLED_EVENT;

    @Before
    public void setUp() throws Exception {
        eventBus = new EventBus();
        EventListener<BookingCancelledEvent> eventListener = new BookingCancelledEventListener(eventBus);
        eventListener.addConsumer(event -> result = BOOKING_CANCELLED.name());
        eventBus.register(eventListener);
    }

    @Test
    public void receiveBookingCancelledEvent() throws Exception {
        eventBus.post(BOOKING_CANCELLED_EVENT);

        assertThat(result).isEqualTo(BOOKING_CANCELLED.name());
    }
}
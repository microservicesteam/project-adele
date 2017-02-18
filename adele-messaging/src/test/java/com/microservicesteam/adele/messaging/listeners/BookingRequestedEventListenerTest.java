package com.microservicesteam.adele.messaging.listeners;

import static com.microservicesteam.adele.messaging.events.EventType.BOOKING_REQUESTED;
import static org.assertj.core.api.Assertions.assertThat;

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
    private String result = "";

    @Mock
    private BookingRequestedEvent BOOKING_REQUESTED_EVENT;

    @Before
    public void setUp() throws Exception {
        eventBus = new EventBus();
        EventListener<BookingRequestedEvent> eventListener = new BookingRequestedEventListener(eventBus);
        eventListener.addConsumer(event -> result = BOOKING_REQUESTED.name());
        eventBus.register(eventListener);
    }

    @Test
    public void receiveBookingCancelledEvent() throws Exception {
        eventBus.post(BOOKING_REQUESTED_EVENT);

        assertThat(result).isEqualTo(BOOKING_REQUESTED.name());
    }
}
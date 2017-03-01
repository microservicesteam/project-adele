package com.microservicesteam.adele.messaging.listeners;

import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.microservicesteam.adele.messaging.events.BookingRequestedEvent;

@Component
public class BookingRequestedEventListener extends EventListener<BookingRequestedEvent> {

    protected BookingRequestedEventListener(EventBus eventBus) {
        super(eventBus);
    }

    @Subscribe
    public void handleEvent(BookingRequestedEvent event) {
        this.consumers.forEach(eConsumer -> eConsumer.accept(event));
    }

}

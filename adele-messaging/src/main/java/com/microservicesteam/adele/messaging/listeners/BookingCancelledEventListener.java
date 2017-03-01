package com.microservicesteam.adele.messaging.listeners;

import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.microservicesteam.adele.messaging.events.BookingCancelledEvent;

@Component
public class BookingCancelledEventListener extends EventListener<BookingCancelledEvent> {

    protected BookingCancelledEventListener(EventBus eventBus) {
        super(eventBus);
    }

    @Subscribe
    public void handleEvent(BookingCancelledEvent event) {
        this.consumers.forEach(eConsumer -> eConsumer.accept(event));
    }

}

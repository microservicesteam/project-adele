package com.microservicesteam.adele.messaging.listeners;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

@Component
public class DeadEventListener {

    private final EventBus eventBus;
    public final List<DeadEvent> deadEvents = new ArrayList<>();

    public DeadEventListener(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Subscribe
    public void handleEvent(DeadEvent event) {
        deadEvents.add(event);
    }

    @PostConstruct
    public void init() {
        eventBus.register(this);
    }

}

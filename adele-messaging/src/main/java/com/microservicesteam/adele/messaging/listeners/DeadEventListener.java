package com.microservicesteam.adele.messaging.listeners;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

@Component
public class DeadEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeadEventListener.class);

    private final EventBus eventBus;
    public final List<DeadEvent> deadEvents = new ArrayList<>();

    public DeadEventListener(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Subscribe
    public void handleEvent(DeadEvent event) {
        LOGGER.debug("Dead event: {}", event);
        deadEvents.add(event);
    }

    @PostConstruct
    public void init() {
        eventBus.register(this);
    }

}

package com.microservicesteam.adele.messaging.listeners;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import com.google.common.eventbus.EventBus;
import com.microservicesteam.adele.messaging.events.Event;

abstract class EventListener<E extends Event> {

    private final EventBus eventBus;
    final List<Consumer<E>> consumers = newArrayList();

    EventListener(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @PostConstruct
    public void init() {
        eventBus.register(this);
        System.out.println("Instance created");
    }

    public void addConsumer(Consumer<E> consumer) {
        this.consumers.add(consumer);
    }

}

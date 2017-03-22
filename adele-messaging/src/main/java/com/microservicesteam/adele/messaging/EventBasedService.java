package com.microservicesteam.adele.messaging;

import javax.annotation.PostConstruct;

import com.google.common.eventbus.EventBus;

public abstract class EventBasedService {

    protected final EventBus eventBus;

    protected EventBasedService(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @PostConstruct
    public void init() {
        eventBus.register(this);
    }

}

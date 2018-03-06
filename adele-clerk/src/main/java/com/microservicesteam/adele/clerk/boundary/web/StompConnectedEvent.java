package com.microservicesteam.adele.clerk.boundary.web;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class StompConnectedEvent implements ApplicationListener<SessionConnectedEvent> {

    @Override
    public void onApplicationEvent(SessionConnectedEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

        log.debug("Connected event [sessionId: " + sha.getSessionId() + " ]");
    }
}
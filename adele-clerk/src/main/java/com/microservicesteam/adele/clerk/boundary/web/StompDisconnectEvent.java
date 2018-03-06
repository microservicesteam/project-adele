package com.microservicesteam.adele.clerk.boundary.web;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class StompDisconnectEvent implements ApplicationListener<SessionDisconnectEvent> {

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

        log.debug("Disconnect event [sessionId: " + sha.getSessionId() + " ]");
    }
}
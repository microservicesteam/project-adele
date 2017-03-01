package com.microservicesteam.adele.messaging.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.eventbus.EventBus;

@Configuration
public class MessagingConfig {

    @Bean
    EventBus eventBus() {
        return new EventBus();
    }

}

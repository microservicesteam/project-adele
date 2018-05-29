package com.microservicesteam.adele.configuration;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdeleConfiguration {

    @Bean
    public Supplier<String> uuidGenerator() {
        return () -> UUID.randomUUID().toString();
    }

    @Bean
    public Supplier<LocalDateTime> currentLocalDateTime() {
        return () -> LocalDateTime.now();
    }

}

package com.microservicesteam.adele.clerk.infrastucture.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.guava.GuavaModule;

@Configuration
public class GuavaModuleConfig {

    @Bean
    public Module guavaModule() {
        return new GuavaModule();
    }

}

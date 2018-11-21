package com.microservicesteam.adele.ordermanager.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@EnableConfigurationProperties(OrderConfiguration.OrderProperties.class)
public class OrderConfiguration {

    @Data
    @ConfigurationProperties("order")
    public static class OrderProperties {
        private String domainUrl;
        private long lifeTime;
        private String successPage;
    }

}

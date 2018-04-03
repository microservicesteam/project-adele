package com.microservicesteam.adele.payment;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@EnableConfigurationProperties(PaypalConfig.PaypalProperties.class)
public class PaypalConfig {

    @Data
    @ConfigurationProperties("paypal")
    public static class PaypalProperties {
        private String clientSecret;
        private String clientId;
        private String mode;
    }

}

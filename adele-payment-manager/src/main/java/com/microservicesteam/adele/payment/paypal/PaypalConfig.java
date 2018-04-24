package com.microservicesteam.adele.payment.paypal;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.paypal.base.rest.APIContext;
import lombok.Data;

@Configuration
@EnableConfigurationProperties(PaypalConfig.PaypalProperties.class)
class PaypalConfig {

    @Data
    @ConfigurationProperties("paypal")
    static class PaypalProperties {
        private String clientSecret;
        private String clientId;
        private String mode;
    }

    @Bean
    public APIContext apiContext(PaypalProperties paypalProperties) {
        return new APIContext(paypalProperties.clientId, paypalProperties.clientSecret, paypalProperties.mode);
    }
}

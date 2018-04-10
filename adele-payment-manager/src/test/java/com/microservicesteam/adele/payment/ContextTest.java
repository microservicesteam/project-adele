package com.microservicesteam.adele.payment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration
public class ContextTest {

    @Autowired
    private PaymentManager paymentManager;

    //TODO: fix loading properties
    @Ignore
    @Test
    public void context() {
        assertThat(paymentManager).isNotNull();
    }

    @Configuration
    @ComponentScan("com.microservicesteam.adele.payment")
    public static class SpringConfig {

    }

}

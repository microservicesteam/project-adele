package com.microservicesteam.adele.ordermanager.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Import(OrderConfiguration.class)
@TestPropertySource(value = "classpath:application.properties")
public class OrderConfigurationTest {

    private static final String DOMAIN_URL = "adele.hu";
    private static final int LIFETIME = 12;
    private static final String SUCCESS_PAGE = "successPage";

    @Autowired
    private OrderConfiguration.OrderProperties orderProperties;

    @Test
    public void properties() {
        assertThat(orderProperties.getDomainUrl()).isEqualTo(DOMAIN_URL);
        assertThat(orderProperties.getLifeTime()).isEqualTo(LIFETIME);
        assertThat(orderProperties.getSuccessPage()).isEqualTo(SUCCESS_PAGE);
    }
}
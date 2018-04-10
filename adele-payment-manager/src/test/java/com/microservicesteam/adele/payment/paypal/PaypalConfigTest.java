package com.microservicesteam.adele.payment.paypal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Import(PaypalConfig.class)
@TestPropertySource(value = "classpath:application.properties")
public class PaypalConfigTest {

    private static final String CLIENT_ID = "clientId";
    private static final String CLIENT_SECRET = "clientSecret";
    private static final String MODE = "sandbox";

    @Autowired
    private PaypalConfig.PaypalProperties paypalProperties;

    @Test
    public void properties() {
        assertThat(paypalProperties.getClientId()).isEqualTo(CLIENT_ID);
        assertThat(paypalProperties.getClientSecret()).isEqualTo(CLIENT_SECRET);
        assertThat(paypalProperties.getMode()).isEqualTo(MODE);
    }
}
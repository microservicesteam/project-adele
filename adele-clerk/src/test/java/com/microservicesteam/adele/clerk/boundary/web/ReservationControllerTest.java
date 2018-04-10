package com.microservicesteam.adele.clerk.boundary.web;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.microservicesteam.adele.clerk.domain.ReservationRequested;
import com.microservicesteam.adele.clerk.domain.ReservationsService;
import com.microservicesteam.adele.clerk.infrastucture.config.GuavaModuleConfig;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationsService reservationsService;

    private MediaType contentType = new MediaType(APPLICATION_JSON.getType(),
            APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));


    @Test
    public void reserveTicketsShouldReturnWithReservationId() throws Exception {
        when(reservationsService.reserveTickets(any()))
                .thenReturn(ReservationRequested.builder()
                        .reservationId("someReservationId")
                        .build());
        String requestBody = "{\"tickets\":[{\"programId\":1,\"sectorId\":2,\"seatId\":3}]}";
        System.out.println(requestBody);
        mockMvc.perform(post("/reservations").accept(APPLICATION_JSON).content(requestBody).contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId", equalTo("someReservationId")));
    }

    //TODO ZM add test when response is ReservationRejected and Validation failed

    @Import(GuavaModuleConfig.class)
    @SpringBootApplication(scanBasePackages = "com.microservicesteam.adele")
    static class TestConfiguration {
    }
}
package com.microservicesteam.adele.booking.boundary.web;

import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import com.google.common.collect.ImmutableList;
import com.microservicesteam.adele.booking.domain.BookingRequested;
import com.microservicesteam.adele.booking.domain.BookingService;
import com.microservicesteam.adele.booking.infrastucture.config.GuavaModuleConfig;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = TicketController.class)
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    private MediaType contentType = new MediaType(APPLICATION_JSON.getType(),
            APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Test
    public void getTicketsStatusShouldReturnWithStatusAndPositionWhenStatusIsPresent() throws Exception {
        when(bookingService.getTicketsStatusByEvent(1)).thenReturn(
                ImmutableList.of(FreeTicket.builder()
                        .position(Position.builder()
                                .id(1)
                                .sectorId(2)
                                .eventId(1)
                                .build())
                        .build()));
        when(bookingService.getTicketsStatusByEvent(2)).thenReturn(
                ImmutableList.of(FreeTicket.builder()
                        .position(Position.builder()
                                .id(2)
                                .sectorId(2)
                                .eventId(2)
                                .build())
                        .build()));
        mockMvc.perform(get("/bookings?eventId=1").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status", equalTo(FREE.name())))
                .andExpect(jsonPath("$[0].position.id", equalTo(1)))
                .andExpect(jsonPath("$[0].position.sectorId", equalTo(2)))
                .andExpect(jsonPath("$[0].position.eventId", equalTo(1)));
    }

    @Test
    public void getTicketsStatusShouldReturnWithEmptyArrayWhenThereAreNoTickets() throws Exception {
        when(bookingService.getTicketsStatusByEvent(1)).thenReturn(ImmutableList.of());
        mockMvc.perform(get("/bookings?eventId=1").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void getTicketsStatusShouldAcceptOptionalSectorIdParameter() throws Exception {
        when(bookingService.getTicketsStatusByEventAndSector(1, 1)).thenReturn(
                ImmutableList.of(FreeTicket.builder()
                        .position(Position.builder()
                                .id(1)
                                .sectorId(1)
                                .eventId(1)
                                .build())
                        .build()));
        when(bookingService.getTicketsStatusByEventAndSector(1, 2)).thenReturn(
                ImmutableList.of(FreeTicket.builder()
                        .position(Position.builder()
                                .id(2)
                                .sectorId(2)
                                .eventId(1)
                                .build())
                        .build()));
        mockMvc.perform(get("/bookings?eventId=1&sectorId=2").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status", equalTo(FREE.name())))
                .andExpect(jsonPath("$[0].position.id", equalTo(2)))
                .andExpect(jsonPath("$[0].position.sectorId", equalTo(2)))
                .andExpect(jsonPath("$[0].position.eventId", equalTo(1)));
    }

    @Test
    public void bookTicketsShouldReturnWithBookingId() throws Exception {
        when(bookingService.bookTickets(any()))
                .thenReturn(BookingRequested.builder()
                        .bookingId("randomBookingId")
                        .build());
        String requestBody = "{\"eventId\":1,\"sectorId\":2,\"positions\":[3]}";
        System.out.println(requestBody);
        mockMvc.perform(post("/bookings").accept(APPLICATION_JSON).content(requestBody).contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId", equalTo("randomBookingId")));
    }

    @Import(GuavaModuleConfig.class)
    @SpringBootApplication(scanBasePackages = "com.microservicesteam.adele")
    static class TestConfiguration {
    }

}
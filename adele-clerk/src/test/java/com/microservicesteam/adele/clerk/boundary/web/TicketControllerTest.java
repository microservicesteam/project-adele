package com.microservicesteam.adele.clerk.boundary.web;

import static com.microservicesteam.adele.ticketmaster.model.TicketStatus.FREE;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.google.common.collect.ImmutableList;
import com.microservicesteam.adele.clerk.domain.TicketsService;
import com.microservicesteam.adele.clerk.infrastucture.config.GuavaModuleConfig;
import com.microservicesteam.adele.ticketmaster.model.Ticket;
import com.microservicesteam.adele.ticketmaster.model.TicketId;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = TicketController.class)
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketsService ticketsService;

    @Test
    public void getTicketsStatusShouldReturnWithTicketDataWhenIsPresent() throws Exception {
        when(ticketsService.getTicketsStatusByProgram(1)).thenReturn(
                ImmutableList.of(Ticket.builder()
                        .status(FREE)
                        .ticketId(TicketId.builder()
                                .seatId(1)
                                .sectorId(2)
                                .programId(1)
                                .build())
                        .build()));
        when(ticketsService.getTicketsStatusByProgram(2)).thenReturn(
                ImmutableList.of(Ticket.builder()
                        .status(FREE)
                        .ticketId(TicketId.builder()
                                .seatId(2)
                                .sectorId(2)
                                .programId(2)
                                .build())
                        .build()));
        mockMvc.perform(get("/tickets?programId=1").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status", equalTo("FREE")))
                .andExpect(jsonPath("$[0].ticketId.seatId", equalTo(1)))
                .andExpect(jsonPath("$[0].ticketId.sectorId", equalTo(2)))
                .andExpect(jsonPath("$[0].ticketId.programId", equalTo(1)));
    }

    @Test
    public void getTicketsStatusShouldReturnWithEmptyArrayWhenThereAreNoTickets() throws Exception {
        when(ticketsService.getTicketsStatusByProgram(1)).thenReturn(ImmutableList.of());
        mockMvc.perform(get("/tickets?programId=1").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void getTicketsStatusShouldAcceptOptionalSectorIdParameter() throws Exception {
        when(ticketsService.getTicketsStatusByProgramAndSector(1, 1)).thenReturn(
                ImmutableList.of(Ticket.builder()
                        .status(FREE)
                        .ticketId(TicketId.builder()
                                .seatId(1)
                                .sectorId(1)
                                .programId(1)
                                .build())
                        .build()));
        when(ticketsService.getTicketsStatusByProgramAndSector(1, 2)).thenReturn(
                ImmutableList.of(Ticket.builder()
                        .status(FREE)
                        .ticketId(TicketId.builder()
                                .seatId(2)
                                .sectorId(2)
                                .programId(1)
                                .build())
                        .build()));
        mockMvc.perform(get("/tickets?programId=1&sectorId=2").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status", equalTo("FREE")))
                .andExpect(jsonPath("$[0].ticketId.seatId", equalTo(2)))
                .andExpect(jsonPath("$[0].ticketId.sectorId", equalTo(2)))
                .andExpect(jsonPath("$[0].ticketId.programId", equalTo(1)));
    }

    @Import(GuavaModuleConfig.class)
    @SpringBootApplication(scanBasePackages = "com.microservicesteam.adele")
    static class TestConfiguration {
    }

}
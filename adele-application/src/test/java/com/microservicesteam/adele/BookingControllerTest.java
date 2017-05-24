package com.microservicesteam.adele;

import com.google.common.collect.ImmutableList;
import com.microservicesteam.adele.booking.boundary.web.BookingController;
import com.microservicesteam.adele.booking.domain.BookingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Test
    public void test() throws Exception {
        Mockito.when(bookingService.getTicketsStatus()).thenReturn(ImmutableList.of());
        mockMvc.perform(get("/events/1/tickets").accept(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
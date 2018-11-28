package com.microservicesteam.adele.ordermanager.boundary.web;

import com.microservicesteam.adele.ordermanager.domain.ApproveUrlResponse;
import com.microservicesteam.adele.ordermanager.domain.OrderConfiguration;
import com.microservicesteam.adele.ordermanager.domain.OrderService;
import com.microservicesteam.adele.ordermanager.domain.exception.InvalidPaymentResponseException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = OrderController.class)
public class OrderControllerTest {

    private static final String ORDER_ID = "6489d903-c07c-48d3-81f9-2d8251b1d3b6";
    private static final String APPROVE_URL = "approveUrl";
    private static final String SUCCESS_PAGE = "successPage";
    private static final String PAYMENT_ID_VALUE = "paymentIdValue";
    private static final String PAYMENT_ID = "paymentId";
    private static final String STATUS = "status";
    private static final String STATUS_VALUE = "statusValue";
    private static final String PAYER_ID_VALUE = "payerIdValue";
    private static final String PAYER_ID = "payerId";

    private MediaType contentType = new MediaType(APPLICATION_JSON.getType(),
            APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderConfiguration.OrderProperties orderProperties;

    @Test
    public void postOrderShouldSaveTheOrder() throws Exception {
        when(orderService.saveOrder(any())).thenReturn(ORDER_ID);
        String requestBody = "{\"name\":\"name\",\"email\":\"email\",\"reservationId\":\"cef758d6-29cf-40c8-ba91-f2a68aa6ecf7\"}";

        mockMvc.perform(post("/orders").accept(contentType).content(requestBody).contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().string(ORDER_ID));
    }

    @Test
    public void getApproveUrlShouldReturnWithInternalServerErrorWhenPaymentResponseIsInvalid() throws Exception{
        when(orderService.initiatePayment(anyString())).thenThrow(
                new InvalidPaymentResponseException("Invalid payment"));

        mockMvc.perform(get("/orders/{orderId}/approval", ORDER_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void getApproveUrlShouldReturnWithApprovalUrl() throws Exception{
        when(orderService.initiatePayment(anyString())).thenReturn(ApproveUrlResponse.builder()
                .approveUrl(APPROVE_URL)
                .build());

        mockMvc.perform(get("/orders/{orderId}/approval", ORDER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.approveUrl", equalTo(APPROVE_URL)));
    }

    @Test
    public void handlePaymentShouldReturnLocationToSuccessPage() throws Exception{
        when(orderProperties.getSuccessPage()).thenReturn(SUCCESS_PAGE);

        mockMvc.perform(get("/orders/{orderId}/payment", ORDER_ID)
                .param(PAYMENT_ID, PAYMENT_ID_VALUE)
                .param(STATUS, STATUS_VALUE)
                .param(PAYER_ID, PAYER_ID_VALUE))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(header().string(HttpHeaders.LOCATION, equalTo(SUCCESS_PAGE)));

        verify(orderService).handlePayment(ORDER_ID, PAYMENT_ID_VALUE, PAYER_ID_VALUE, STATUS_VALUE);
    }

    @SpringBootApplication(scanBasePackages = "com.microservicesteam.adele")
    static class TestConfiguration {
    }

}
package com.microservicesteam.adele.ordermanager.boundary.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.microservicesteam.adele.ordermanager.domain.ApproveUrlResponse;
import com.microservicesteam.adele.ordermanager.domain.OrderService;
import com.microservicesteam.adele.ordermanager.domain.PostOrderRequest;
import com.microservicesteam.adele.ordermanager.domain.exception.InvalidPaymentResponseException;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public String postOrder(@RequestBody PostOrderRequest postOrderRequest) {
        return orderService.saveOrder(postOrderRequest);
    }

    @GetMapping("{orderId}/approval")
    public ApproveUrlResponse getApproveUrl(@PathVariable("orderId") String orderId) {
        return orderService.initiatePayment(orderId);
    }

    @GetMapping("{orderId}/payment")
    public ResponseEntity handlePayment(@PathVariable("orderId") String orderId,
            @RequestParam(name = "paymentId") String paymentId,
            @RequestParam(name = "status") String status,
            @RequestParam(name = "payerId") String payerId) {
        // TODO add proper location
        String location = "payment result page";
        orderService.handlePayment(orderId, paymentId, payerId, status);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, location)
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InvalidPaymentResponseException.class)
    public void handleInvalidPaymentResponseException() {
        // TODO Redirect to error page
    }


}

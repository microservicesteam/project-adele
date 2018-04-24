package com.microservicesteam.adele.ordermanager.boundary.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservicesteam.adele.ordermanager.domain.OrderService;
import com.microservicesteam.adele.ordermanager.domain.PostOrderRequest;
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

}

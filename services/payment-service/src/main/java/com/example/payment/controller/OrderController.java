package com.example.payment.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.platform.observability.annotation.Track;


@RestController
@RequestMapping("/orders")
public class OrderController {

    
    @GetMapping
    @Track("getOrders")
    public String getOrders() {
        return "orders";
    }

    @GetMapping
    @Track("order")
    public String createOrder() {
        return "Order created ✅";
    }

    @GetMapping
    @Track("fail")
    public String fail() {
        return "Simulated failure ❌";
    }

}

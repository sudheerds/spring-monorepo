package com.example.order.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.example.observability.annotation.Track;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final RestClient paymentClient;

    public OrderController(RestClient.Builder restClientBuilder, 
                           @Value("${app.services.payment.url:http://localhost:8081/api}") String paymentServiceUrl) {
        this.paymentClient = restClientBuilder.baseUrl(paymentServiceUrl).build();
    }

    @GetMapping("/getOrders")
    @PreAuthorize("hasRole('USER')")
    @Track("getOrders")
    public String getOrders() {
        return "Shoes and Shoes";
    }

    @PostMapping("/createOrder")
    @PreAuthorize("hasRole('ADMIN')")
    @Track("createOrder")
    public String createOrder() {
        return "Order created ✅";
    }

    @GetMapping("/test-payment-call")
    @PreAuthorize("hasRole('USER')")
    @Track("test-payment-call")
    public String testPaymentCall() {
        return paymentClient.get()
                .uri("/payments/getPayments")
                .retrieve()
                .body(String.class);
    }

}
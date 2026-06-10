package com.example.order.controller;

import com.example.order.entity.Order;
import com.example.order.repository.OrderRepository;
import com.example.observability.annotation.Track;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final RestClient paymentClient;
    private final OrderRepository orderRepository;

    public OrderController(RestClient.Builder restClientBuilder, 
                           OrderRepository orderRepository,
                           @Value("${app.services.payment.url:http://localhost:8081/api}") String paymentServiceUrl) {
        this.paymentClient = restClientBuilder.baseUrl(paymentServiceUrl).build();
        this.orderRepository = orderRepository;
    }

    @GetMapping("/getOrders")
    @PreAuthorize("hasRole('USER')")
    @Track("getOrders")
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @PostMapping("/createOrder")
    @PreAuthorize("hasRole('ADMIN')")
    @Track("createOrder")
    public Order createOrder(@RequestParam(defaultValue = "Shoes") String product, 
                             @RequestParam(defaultValue = "120.0") Double price) {
        Order order = new Order();
        order.setProduct(product);
        order.setPrice(price);
        return orderRepository.save(order);
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
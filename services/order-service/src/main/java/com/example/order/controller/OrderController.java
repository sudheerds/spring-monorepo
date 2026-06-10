package com.example.order.controller;

import com.example.order.entity.Order;
import com.example.order.repository.OrderRepository;
import com.example.order.event.OrderCreatedEvent;
import com.example.order.config.OrderMessagingConfig;
import com.example.observability.annotation.Track;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import com.example.validation.annotation.Alphanumeric;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Validated
public class OrderController {

    private final RestClient paymentClient;
    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    public OrderController(RestClient.Builder restClientBuilder, 
                           OrderRepository orderRepository,
                           RabbitTemplate rabbitTemplate,
                           @Value("${app.services.payment.url:http://localhost:8081/api}") String paymentServiceUrl) {
        this.paymentClient = restClientBuilder.baseUrl(paymentServiceUrl).build();
        this.orderRepository = orderRepository;
        this.rabbitTemplate = rabbitTemplate;
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
    public Order createOrder(@RequestParam @NotBlank(message = "Product name cannot be blank") 
                             @Alphanumeric(message = "Product name must be alphanumeric") String product, 
                             @RequestParam @Positive(message = "Price must be positive") Double price) {
        Order order = new Order();
        order.setProduct(product);
        order.setPrice(price);
        Order savedOrder = orderRepository.save(order);
        try {
            OrderCreatedEvent event = new OrderCreatedEvent(savedOrder.getId(), savedOrder.getProduct(), savedOrder.getPrice());
            rabbitTemplate.convertAndSend(OrderMessagingConfig.EXCHANGE, OrderMessagingConfig.ROUTING_KEY, event);
        } catch (Exception e) {
            // Log warning/error
        }
        return savedOrder;
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
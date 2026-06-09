package com.example.order.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.observability.annotation.Track;

@RestController
@RequestMapping("/orders")
public class OrderController{

    
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

    @GetMapping("/fail")
    @Track("fail")
    public String fail() {
        return "Order failure ❌";
    }

}
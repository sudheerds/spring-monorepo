package com.example.order.controller;

import com.example.platform.observability.annotation.Track;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController{

    
    @GetMapping("/getOrders")
    @Track("getOrders")
    public String getOrders() {
        return "Shoes and Shoes";
    }

    @PostMapping("/createOrder")
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
package com.example.payment.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.platform.observability.annotation.Track;


@RestController
@RequestMapping("/payments")
public class PaymentController{

    
    @GetMapping("/getPayments")
    @Track("getPayments")
    public String getPayments() {
        return "Visa and ruPay";
    }

    @PostMapping("/doPayment")
    @Track("doPayment")
    public String doPayment() {
        return "Payment created ✅";
    }

    @GetMapping("/fail")
    @Track("fail")
    public String fail() {
        return "Payment failure ❌";
    }

}

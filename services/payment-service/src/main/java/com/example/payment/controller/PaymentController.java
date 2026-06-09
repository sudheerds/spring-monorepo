package com.example.payment.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.observability.annotation.Track;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @GetMapping("/getPayments")
    @PreAuthorize("hasRole('USER')")
    @Track("getPayments")
    public String getPayments() {
        return "Visa and ruPay";
    }

    @PostMapping("/doPayment")
    @PreAuthorize("hasRole('ADMIN')")
    @Track("doPayment")
    public String doPayment() {
        return "Payment created ✅";
    }

}

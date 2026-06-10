package com.example.payment.controller;

import com.example.payment.entity.Payment;
import com.example.payment.repository.PaymentRepository;
import com.example.observability.annotation.Track;
import com.example.validation.annotation.EnumValue;
import com.example.payment.entity.PaymentStatus;
import jakarta.validation.constraints.Positive;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/payments")
@Validated
public class PaymentController {

    private final PaymentRepository paymentRepository;

    public PaymentController(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @GetMapping("/getPayments")
    @PreAuthorize("hasRole('USER')")
    @Track("getPayments")
    public List<Payment> getPayments() {
        return paymentRepository.findAll();
    }

    @PostMapping("/doPayment")
    @PreAuthorize("hasRole('ADMIN')")
    @Track("doPayment")
    public Payment doPayment(@RequestParam @Positive(message = "Payment amount must be positive") Double amount,
                             @RequestParam @EnumValue(enumClass = PaymentStatus.class, message = "Status must be SUCCESS, FAILED, COMPLETED, or PENDING", ignoreCase = true) String status) {
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setStatus(status);
        return paymentRepository.save(payment);
    }

}

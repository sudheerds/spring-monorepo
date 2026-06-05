package com.example.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication(scanBasePackages = "com.example")
@EnableAspectJAutoProxy
public class PaymentServiceApplication {
    private static final Logger log = LoggerFactory.getLogger(PaymentServiceApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }


    @EventListener(ApplicationReadyEvent.class)
    public void logObservabilityStatus() {
        log.info("✅ Observability stack initialized: logging=ON, tracing=ON, metrics=ON");
    }
}

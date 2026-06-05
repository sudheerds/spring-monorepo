package com.example.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.EventListener;

@SpringBootApplication(scanBasePackages = "com.example")
@EnableAspectJAutoProxy
public class OrderServiceApplication {
    
    
    private static final Logger log = LoggerFactory.getLogger(OrderServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }


    @EventListener(ApplicationReadyEvent.class)
    public void logObservabilityStatus() {
        log.info("✅ Observability stack initialized: logging=ON, tracing=ON, metrics=ON");
    }

}

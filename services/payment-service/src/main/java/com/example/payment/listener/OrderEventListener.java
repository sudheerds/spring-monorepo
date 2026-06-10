package com.example.payment.listener;

import com.example.payment.config.PaymentMessagingConfig;
import com.example.payment.entity.Payment;
import com.example.payment.event.OrderCreatedEvent;
import com.example.payment.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.messaging.enabled", havingValue = "true", matchIfMissing = true)
public class OrderEventListener {

    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);
    private final PaymentRepository paymentRepository;

    public OrderEventListener(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @RabbitListener(queues = PaymentMessagingConfig.QUEUE)
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent for Order ID: {} | Product: {} | Price: {}", 
                event.getOrderId(), event.getProduct(), event.getPrice());
        
        Payment payment = new Payment();
        payment.setAmount(event.getPrice());
        payment.setStatus("COMPLETED");
        
        Payment savedPayment = paymentRepository.save(payment);
        log.info("Asynchronously created payment ID: {} for Order ID: {}", 
                savedPayment.getId(), event.getOrderId());
    }
}

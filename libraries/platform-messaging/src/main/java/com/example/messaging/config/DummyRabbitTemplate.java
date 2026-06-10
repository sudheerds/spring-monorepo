package com.example.messaging.config;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class DummyRabbitTemplate extends RabbitTemplate {

    @Override
    public void send(String exchange, String routingKey, Message message, CorrelationData correlationData) throws AmqpException {
        // No-op
    }

    @Override
    public void convertAndSend(String exchange, String routingKey, Object message, MessagePostProcessor messagePostProcessor, CorrelationData correlationData) throws AmqpException {
        // No-op
    }

    @Override
    public void convertAndSend(String exchange, String routingKey, Object message) throws AmqpException {
        // No-op
    }

    @Override
    public void convertAndSend(String exchange, String routingKey, Object message, MessagePostProcessor messagePostProcessor) throws AmqpException {
        // No-op
    }

    @Override
    public void afterPropertiesSet() {
        // No-op to prevent validation of ConnectionFactory in test environments
    }
}

package com.example.messaging.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "app.messaging.enabled", havingValue = "false")
public class PlatformMessagingMockConfig {

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new DummyRabbitTemplate();
    }
}

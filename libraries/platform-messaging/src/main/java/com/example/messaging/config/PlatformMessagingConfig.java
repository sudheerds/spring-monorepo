package com.example.messaging.config;

import com.example.messaging.trace.AmqpTraceAdvice;
import com.example.messaging.trace.AmqpTracePostProcessor;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "app.messaging.enabled", havingValue = "true", matchIfMissing = true)
public class PlatformMessagingConfig {

    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        if (objectMapper instanceof JsonMapper jsonMapper) {
            return new JacksonJsonMessageConverter(jsonMapper);
        }
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, 
                                         JacksonJsonMessageConverter jsonMessageConverter,
                                         AmqpTracePostProcessor tracePostProcessor) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        template.setBeforePublishPostProcessors(tracePostProcessor);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            JacksonJsonMessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        factory.setAdviceChain(new AmqpTraceAdvice());
        factory.setDefaultRequeueRejected(false); // Prevents infinite retry loops on consumer errors
        return factory;
    }
}

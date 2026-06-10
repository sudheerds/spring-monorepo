package com.example.messaging.trace;

import org.slf4j.MDC;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

@Component
public class AmqpTracePostProcessor implements MessagePostProcessor {
    public static final String TRACE_HEADER = "x-trace-id";

    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        String traceId = MDC.get("traceId");
        if (traceId != null) {
            message.getMessageProperties().setHeader(TRACE_HEADER, traceId);
        }
        return message;
    }
}

package com.example.messaging.trace;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import java.util.UUID;

public class AmqpTraceAdvice implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object[] args = invocation.getArguments();
        String traceId = null;

        for (Object arg : args) {
            if (arg instanceof Message) {
                Message message = (Message) arg;
                Object header = message.getMessageProperties().getHeader(AmqpTracePostProcessor.TRACE_HEADER);
                if (header != null) {
                    traceId = header.toString();
                }
            }
        }

        if (traceId == null) {
            traceId = UUID.randomUUID().toString(); // Fallback trace
        }

        MDC.put("traceId", traceId);
        try {
            return invocation.proceed();
        } finally {
            MDC.remove("traceId");
        }
    }
}

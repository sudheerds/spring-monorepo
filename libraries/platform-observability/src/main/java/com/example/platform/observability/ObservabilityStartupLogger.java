package com.example.platform.observability;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.tracing.Tracer;

@Component
public class ObservabilityStartupLogger {

    private static final Logger log = LoggerFactory.getLogger(ObservabilityStartupLogger.class);

    @Autowired(required = false)
    private MeterRegistry meterRegistry;

    @Autowired(required = false)
    private Tracer tracer;

    @EventListener(ApplicationReadyEvent.class)
    public void logStatus() {
        log.info("✅ Observability initialized → metrics={}, tracing={}, logging={}",
            meterRegistry != null ? "ON" : "OFF",
            tracer != null ? "ON" : "OFF",
            "ON"
        );

    if (meterRegistry != null) {
            log.info("✅ Metrics backend: {}", meterRegistry.getClass().getSimpleName());
        }

    }
}
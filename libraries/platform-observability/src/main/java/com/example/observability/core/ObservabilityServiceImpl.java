package com.example.observability.core;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.observability.config.ObservabilityProperties;

import io.micrometer.core.instrument.MeterRegistry;




@Service
public class ObservabilityServiceImpl implements ObservabilityService {

    private static final Logger log = LoggerFactory.getLogger(ObservabilityServiceImpl.class);
    private final MeterRegistry meterRegistry;
    private final ObservabilityProperties observabilityProperties;
    @Value("${spring.application.name:unknown}")
    private String serviceName;

    public ObservabilityServiceImpl(MeterRegistry meterRegistry, ObservabilityProperties observabilityProperties) {
        this.meterRegistry = meterRegistry;
        this.observabilityProperties = observabilityProperties;
    }

    @Override
    public <T> T track(String operation, SupplierWithException<T> supplier) {

        long startTime = System.currentTimeMillis();
        log.info("🚀 Executing operation: {}", operation);
        log.debug("MeterRegistry = {}", meterRegistry);
        try {

            T result = supplier.get();

            // ✅ SUCCESS COUNTER
            meterRegistry.counter("business.operation.success", "operation",operation + ".success",
                "env", observabilityProperties.getEnv(),
                        "region", observabilityProperties.getRegion(),
                        "service", serviceName,
                        "operation", operation
            ).increment();

            log.info("✅ Operation succeeded: {}", operation);

            return result;

        } catch (Exception e) {

        // ✅ FAILURE COUNTER          

        meterRegistry.counter("business.operation.failure", "operation", operation + ".failure",
        "env", observabilityProperties.getEnv(),
                "region", observabilityProperties.getRegion(),
                "service", serviceName,
                "operation", operation
        ).increment();

        log.error("❌ Operation failed: {}", operation, e);

        throw new RuntimeException(e);

        } finally {
            long duration = System.currentTimeMillis() - startTime;
            // ✅ LATENCY (TIMER)
            meterRegistry.timer("business.operation.latency", "operation", operation + ".latency",
        "env", observabilityProperties.getEnv(),
                "region", observabilityProperties.getRegion(),
                "service", serviceName,
                "operation", operation
            ).record(duration, TimeUnit.MILLISECONDS);
            
            log.info("⏱ Operation {} completed in {} ms", operation, duration);
        }
    }    
}

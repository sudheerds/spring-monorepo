package com.example.controller;

import com.example.platform.observability.core.ObservabilityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    private final ObservabilityService observabilityService;

    public PingController(ObservabilityService observabilityService) {
        this.observabilityService = observabilityService;
    }

    @GetMapping("/ping")
    public String ping() {
        return observabilityService.track("ping", () -> {
            return "Service is alive ✅";
        });
    }
}
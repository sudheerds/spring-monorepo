package com.example.controller;

import com.example.platform.observability.annotation.Track;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @GetMapping("/ping")
    @Track("ping")
    public String ping() {
        return "Service is alive ✅";
    }
}

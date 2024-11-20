package org.example.expert.health.controller;

import org.example.expert.health.dto.HealthDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class HealthController {

    @GetMapping("/health")
    public HealthDto healthCheck() {
        String status = "양호";
        String version = "1.0.0";
        LocalDateTime checkTime = LocalDateTime.now();

        return new HealthDto(status, version, checkTime);
    }
}

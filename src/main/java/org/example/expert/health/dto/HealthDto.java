package org.example.expert.health.dto;


import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class HealthDto {

    private final String status;
    private final String version;
    private final LocalDateTime checkTime;

    public HealthDto(String status, String version, LocalDateTime checkTime) {
        this.status = status;
        this.version = version;
        this.checkTime = checkTime;
    }
}

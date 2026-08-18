package ru.practicum.stat.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class EndpointHitDto {

    private String app;
    private String uri;
    private String ip;
    private Instant timestamp;

}

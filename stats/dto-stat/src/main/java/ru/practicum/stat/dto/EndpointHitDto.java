package ru.practicum.stat.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHitDto {

    private String app;
    private String uri;
    private String ip;
    private Instant timestamp;

}

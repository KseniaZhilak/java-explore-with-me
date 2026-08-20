package ru.practicum.stat.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class StatClient {

    private final RestTemplate rest;

    public StatClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        this.rest = builder.rootUri(serverUrl).build();
    }

    public EndpointHitDto createHit(String app, String uri, String ip) {
        return rest.postForObject("/hit", new EndpointHitDto(app, uri, ip, LocalDateTime.now()), EndpointHitDto.class);
    }

    public List<ViewStatsDto> getHits(LocalDateTime start, LocalDateTime end, Boolean unique, List<String> uris) {
        String uri = "/stats?start={start}&end={end}&unique={unique}&uris={uris}";
        ResponseEntity<List<ViewStatsDto>> response = rest.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<ViewStatsDto>>() {
        });
        return response.getBody() != null ? response.getBody() : Collections.emptyList();
    }

}

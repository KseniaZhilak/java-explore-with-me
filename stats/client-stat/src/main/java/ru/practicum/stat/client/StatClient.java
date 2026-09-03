package ru.practicum.stat.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
public class StatClient {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RestTemplate rest;

    public StatClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        this.rest = builder.rootUri(serverUrl).build();
    }

    public EndpointHitDto createHit(String app, String uri, String ip) {
        return rest.postForObject("/hit", new EndpointHitDto(app, uri, ip, LocalDateTime.now()), EndpointHitDto.class);
    }

    public List<ViewStatsDto> getHits(LocalDateTime start, LocalDateTime end, Boolean unique, List<String> uris) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath("/stats")
                .queryParam("start", start.format(FORMATTER))
                .queryParam("end", end.format(FORMATTER))
                .queryParam("unique", unique);
        if (uris != null && !uris.isEmpty()) {
            uriBuilder.queryParam("uris", uris);
        }

        ResponseEntity<List<ViewStatsDto>> response = rest.exchange(
                uriBuilder.build().toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ViewStatsDto>>() {
                });
        return response.getBody() != null ? response.getBody() : Collections.emptyList();
    }

}

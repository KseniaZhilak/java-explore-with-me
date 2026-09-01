package ru.practicum.stat.controller;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.ViewStatsDto;
import ru.practicum.stat.service.StatService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
public class StatsController {

    private final StatService statService;

    public StatsController(StatService statService) {
        this.statService = statService;
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getListStat(
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime start,
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime end,
            @RequestParam(defaultValue = "false") Boolean unique,
            @RequestParam(defaultValue = "") Collection<String> uris) {
        return statService.getListStat(start, end, unique, uris);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/hit")
    public EndpointHitDto createStat(@RequestBody @Valid EndpointHitDto hitDto) {
        return statService.createHit(hitDto);
    }

}

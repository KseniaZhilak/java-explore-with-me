package ru.practicum.stat.service;

import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StatService {

    List<ViewStatsDto> getListStat(LocalDateTime start, LocalDateTime end, Boolean unique, Collection<String> uris);

    EndpointHitDto createHit(EndpointHitDto hitDto);

}

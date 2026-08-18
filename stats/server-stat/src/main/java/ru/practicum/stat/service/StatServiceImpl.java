package ru.practicum.stat.service;

import org.springframework.stereotype.Service;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
public class StatServiceImpl implements StatService {

    @Override
    public List<ViewStatsDto> getListStat(LocalDateTime start, LocalDateTime end, Boolean unique, Collection<String> uris) {
        return List.of();
    }

    @Override
    public EndpointHitDto createHit(EndpointHitDto hitDto) {
        return null;
    }

}

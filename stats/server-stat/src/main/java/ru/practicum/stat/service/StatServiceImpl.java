package ru.practicum.stat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.ViewStatsDto;
import ru.practicum.stat.repository.HitEntity;
import ru.practicum.stat.repository.HitRepository;
import ru.practicum.stat.repository.mapper.HitMapper;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class StatServiceImpl implements StatService {

    private final HitRepository hitRepository;
    private final HitMapper hitMapper;

    public StatServiceImpl(HitRepository hitRepository, HitMapper hitMapper) {
        this.hitRepository = hitRepository;
        this.hitMapper = hitMapper;
    }

    @Override
    public List<ViewStatsDto> getListStat(LocalDateTime start, LocalDateTime end, Boolean unique, Collection<String> uris) {
        boolean filterByUris = uris != null && !uris.isEmpty();
        if (Boolean.TRUE.equals(unique)) {
            return hitRepository.findUniqueStats(start, end, uris, filterByUris);
        } else {
            return hitRepository.findStats(start, end, uris, filterByUris);
        }
    }

    @Override
    @Transactional
    public EndpointHitDto createHit(EndpointHitDto hitDto) {
        HitEntity hitEntity = hitMapper.toHitEntity(hitDto);
        HitEntity saved = hitRepository.save(hitEntity);
        return hitMapper.toEndpointHitDto(saved);
    }

}

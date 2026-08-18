package ru.practicum.stat.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.repository.HitEntity;

@Mapper
public interface HitMapper {

    @Mapping(target = "id", ignore = true)
    HitEntity toHitEntity(EndpointHitDto hitDto);

    @Mapping(target = "id", ignore = true)
    EndpointHitDto toEndpointHitDto(HitEntity hitEntity);

}

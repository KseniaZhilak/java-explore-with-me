package ru.practicum.stat.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.repository.HitEntity;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface HitMapper {

    @Mapping(target = "id", ignore = true)
    HitEntity toHitEntity(EndpointHitDto hitDto);

    EndpointHitDto toEndpointHitDto(HitEntity hitEntity);

}

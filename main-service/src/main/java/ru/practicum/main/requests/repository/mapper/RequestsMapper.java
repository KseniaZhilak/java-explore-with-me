package ru.practicum.main.requests.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.main.requests.dto.ParticipationRequestDto;
import ru.practicum.main.requests.repository.RequestsEntity;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = SPRING,
        unmappedTargetPolicy = IGNORE)
public interface RequestsMapper {

    @Mapping(target = "created", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "event.id", source = "eventId")
    RequestsEntity toEntity(Long eventId, Long userId);

    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "user.id")
    ParticipationRequestDto toDto(RequestsEntity requestsEntity);

}

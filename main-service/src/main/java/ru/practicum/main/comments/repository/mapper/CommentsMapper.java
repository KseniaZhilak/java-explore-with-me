package ru.practicum.main.comments.repository.mapper;

import org.mapstruct.*;
import ru.practicum.main.comments.dto.CommentsDto;
import ru.practicum.main.comments.dto.CommentsRequestDto;
import ru.practicum.main.comments.dto.CommentsUpdateDto;
import ru.practicum.main.comments.repository.CommentsEntity;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = SPRING,
        unmappedTargetPolicy = IGNORE)
public interface CommentsMapper {

    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "userId", source = "user.id")
    CommentsDto toDto(CommentsEntity entity);

    @Mapping(target = "created", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "event.id", source = "eventId")
    @Mapping(target = "user.id", source = "userId")
    CommentsEntity toEntity(Long eventId, Long userId, CommentsRequestDto commentsRequestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "updated", expression = "java(java.time.LocalDateTime.now())")
    void setCommentsUpdateDto(@MappingTarget CommentsEntity entity, CommentsUpdateDto dto);

}

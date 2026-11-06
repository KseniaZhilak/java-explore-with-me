package ru.practicum.main.compilations.repository.mapper;

import org.mapstruct.*;
import ru.practicum.main.compilations.dto.CompilationDto;
import ru.practicum.main.compilations.dto.NewCompilationDto;
import ru.practicum.main.compilations.dto.UpdateCompilationRequest;
import ru.practicum.main.compilations.repository.CompilationEntity;
import ru.practicum.main.events.repository.mapper.EventsMapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = SPRING,
        unmappedTargetPolicy = IGNORE,
        uses = EventsMapper.class)
public interface CompilationsMapper {

    @Mapping(target = "events", ignore = true)
    CompilationEntity toEntity(NewCompilationDto compilationDto);

    @Mapping(target = "events", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CompilationEntity toEntity(@MappingTarget CompilationEntity updated, UpdateCompilationRequest compilationDto);

    CompilationDto toDto(CompilationEntity compilationEntity);

}

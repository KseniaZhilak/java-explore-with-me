package ru.practicum.main.events.repository.mapper;

import org.mapstruct.*;
import ru.practicum.main.events.dto.*;
import ru.practicum.main.events.repository.EventsEntity;
import ru.practicum.main.users.repository.mapper.UsersMapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = SPRING,
        unmappedTargetPolicy = IGNORE,
        uses = UsersMapper.class)
public interface EventsMapper {

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdOn", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "lat", source = "location.lat")
    @Mapping(target = "lon", source = "location.lon")
    EventsEntity toEntity(NewEventDto newEventDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "lat", source = "location.lat")
    @Mapping(target = "lon", source = "location.lon")
    void updateEntity(UpdateEventUserRequest dto, @MappingTarget EventsEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "lat", source = "location.lat")
    @Mapping(target = "lon", source = "location.lon")
    void updateAdminEntity(UpdateEventAdminRequest dto, @MappingTarget EventsEntity entity);

    @Mapping(source = "user", target = "initiator")
    @Mapping(source = ".", target = "location", qualifiedByName = "toLocationDto")
    EventFullDto toEventFullDto(EventsEntity eventsEntity);

    @Mapping(source = "user", target = "initiator")
    EventShortDto toEventShortDto(EventsEntity eventsEntity);

    @Named("toLocationDto")
    default LocationDto toLocationDto(EventsEntity eventsEntity) {
        if (eventsEntity == null) {
            return null;
        }
        return new LocationDto(
                eventsEntity.getLat() != null ? eventsEntity.getLat().floatValue() : null,
                eventsEntity.getLon() != null ? eventsEntity.getLon().floatValue() : null
        );
    }

}

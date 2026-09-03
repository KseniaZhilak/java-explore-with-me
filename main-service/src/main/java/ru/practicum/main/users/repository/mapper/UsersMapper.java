package ru.practicum.main.users.repository.mapper;

import org.mapstruct.Mapper;
import ru.practicum.main.users.dto.NewUserRequest;
import ru.practicum.main.users.dto.UserDto;
import ru.practicum.main.users.dto.UserShortDto;
import ru.practicum.main.users.repository.UsersEntity;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = SPRING,
        unmappedTargetPolicy = IGNORE)
public interface UsersMapper {

    UsersEntity toEntity(NewUserRequest userDto);

    UsersEntity toEntity(UserShortDto userShortDto);

    UserDto toDto(UsersEntity usersEntity);

}

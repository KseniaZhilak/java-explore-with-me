package ru.practicum.main.users.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.main.users.dto.NewUserRequest;
import ru.practicum.main.users.dto.UserDto;
import ru.practicum.main.users.repository.UsersEntity;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface UsersMapper {

    @Mapping(target = "id", ignore = true)
    UsersEntity toEntity(NewUserRequest userDto);

    UserDto toDto(UsersEntity usersEntity);

}

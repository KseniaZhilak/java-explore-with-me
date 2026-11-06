package ru.practicum.main.users;

import ru.practicum.main.users.dto.NewUserRequest;
import ru.practicum.main.users.dto.UserDto;

import java.util.Collection;
import java.util.List;

public interface UsersService {

    UserDto createUser(NewUserRequest userDto);

    Collection<UserDto> getAllUsers(List<Integer> ids, int offset, int size);

    void deleteUser(Long id);

}

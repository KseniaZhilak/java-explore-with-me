package ru.practicum.main.users;

import org.springframework.stereotype.Service;
import ru.practicum.main.exception.ConflictException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.users.dto.NewUserRequest;
import ru.practicum.main.users.dto.UserDto;
import ru.practicum.main.users.repository.UsersEntity;
import ru.practicum.main.users.repository.UsersRepository;
import ru.practicum.main.users.repository.mapper.UsersMapper;

import java.util.Collection;
import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;

    public UsersServiceImpl(UsersRepository usersRepository, UsersMapper usersMapper) {
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
    }

    @Override
    public UserDto createUser(NewUserRequest userDto) {
        if (usersRepository.existsByEmailEqualsIgnoreCase(userDto.getEmail())) {
            throw new ConflictException("User already exists");
        }
        UsersEntity entity = usersMapper.toEntity(userDto);
        UsersEntity saved = usersRepository.save(entity);
        return usersMapper.toDto(saved);
    }

    @Override
    public Collection<UserDto> getAllUsers(List<Integer> ids, int offset, int size) {
        boolean filterByIds = !ids.isEmpty();
        Collection<UsersEntity> usersByFilters = usersRepository
                .findUsersByFilters(ids, filterByIds, offset, size);
        return usersByFilters.stream().map(usersMapper::toDto).toList();
    }

    @Override
    public void deleteUser(Long id) {
        if (!usersRepository.existsById(id)) {
            throw new NotFoundException("User not found");
        }
        usersRepository.deleteById(id);
    }

}

package ru.practicum.shareit.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserStorage userStorage;
    UserMapper userMapper;

    @Override
    public UserDto addUser(UserDto userDto) {
        return userMapper.apply(userStorage.addUser(userMapper.toUser(userDto)));
    }

    @Override
    public UserDto updateUser(int id, UserDto userDto) {
        userDto.setId(id);
        return userMapper.apply(userStorage.addUser(userMapper.toUser(userDto)));
    }

    @Override
    public UserDto getUserById(int id) {
        return userMapper.apply(userStorage.getUserById(id));
    }

    @Override
    public List<UserDto> getUsers() {
        return userStorage.getUsers().stream().map(userMapper).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }
}

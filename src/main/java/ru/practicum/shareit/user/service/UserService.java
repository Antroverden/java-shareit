package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDto);

    UserDto updateUser(int id, UserDto userDto);

    UserDto getUserById(int id);

    List<UserDto> getUsers();

    void deleteUser(int id);
}

package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.function.Function;

@Service
public class UserMapper implements Function<User, UserDto> {
    @Override
    public UserDto apply(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public User toUser(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }
}

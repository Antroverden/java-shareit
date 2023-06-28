package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;
    UserMapper userMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody UserDto userDto) {
        return userMapper.toDto(userService.addUser(userMapper.toUser(userDto)));
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable int userId, @RequestBody UserDto userDto) {
        userDto.setId(userId);
        return userMapper.toDto(userService.updateUser(userMapper.toUser(userDto)));
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable int userId) {
        return userMapper.toDto(userService.getUserById(userId));
    }

    @GetMapping
    public List<UserDto> getUsers() {
        return userMapper.toDto(userService.getUsers());
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
    }
}

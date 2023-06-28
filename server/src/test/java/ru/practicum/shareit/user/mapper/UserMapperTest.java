package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    @InjectMocks
    private UserMapper userMapper;

    @Test
    public void testToDto_singleUser() {
        User user = new User(1, "John Doe", "john@example.com");
        UserDto userDto = userMapper.toDto(user);

        assertEquals(1, userDto.getId());
        assertEquals("John Doe", userDto.getName());
        assertEquals("john@example.com", userDto.getEmail());
    }

    @Test
    public void testToDto_listOfUsers() {
        List<User> users = Arrays.asList(
                new User(1, "John Doe", "john@example.com"),
                new User(2, "Jane Doe", "jane@example.com")
        );

        List<UserDto> userDtos = userMapper.toDto(users);

        assertEquals(2, userDtos.size());

        assertEquals(1, userDtos.get(0).getId());
        assertEquals("John Doe", userDtos.get(0).getName());
        assertEquals("john@example.com", userDtos.get(0).getEmail());

        assertEquals(2, userDtos.get(1).getId());
        assertEquals("Jane Doe", userDtos.get(1).getName());
        assertEquals("jane@example.com", userDtos.get(1).getEmail());
    }

    @Test
    public void testToUser() {
        UserDto userDto = new UserDto(1, "John Doe", "john@example.com");
        User user = userMapper.toUser(userDto);

        assertEquals(1, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
    }
}

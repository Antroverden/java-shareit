package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

@JsonTest
class UserMapperTest {

//    @Autowired
//    private JacksonTester<UserDto> json;
//
//    @Test
//    void testUserDto() throws Exception {
//        UserDto userDto = new UserDto(
//                1L,
//                "john.doe@mail.com",
//                "John",
//                "Doe",
//                "2022.07.03 19:55:00",
//                UserState.ACTIVE);
//
//        JsonContent<UserDto> result = json.write(userDto);
//
//        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
//        assertThat(result).extractingJsonPathStringValue("$.firstName").isEqualTo("John");
//        assertThat(result).extractingJsonPathStringValue("$.lastName").isEqualTo("Doe");
//        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("john.doe@mail.com");
//    }

    @Test
    void toDto() {
    }

    @Test
    void testToDto() {
    }

    @Test
    void toUser() {
    }
}
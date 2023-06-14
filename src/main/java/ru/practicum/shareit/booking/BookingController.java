package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.booking.Booking.Status;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {

    UserStorage userStorage;

    public User addUser(User user) {
        if (userStorage.getUsers().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Юзер с имейлом " + user.getEmail() + "уже существует");
        }
        return userStorage.addUser(user);
    }


    public User updateUser(User user) {
        if (userStorage.getUsers().stream()
                .anyMatch(u -> (u.getEmail().equals(user.getEmail()) && u.getId() != user.getId()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Юзер с имейлом " + user.getEmail() + "уже существует");
        }
        return userStorage.updateUser(user);
    }


    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }


    public List<User> getUsers() {
        return userStorage.getUsers();
    }


    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }

    enum State {
        ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED
    }
}

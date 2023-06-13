package ru.practicum.shareit.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserStorage userStorage;

    @Override
    public User addUser(User user) {
        if (userStorage.getUsers().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Юзер с имейлом " + user.getEmail() + "уже существует");
        }
        return userStorage.addUser(user);
    }

    @Override
    public User updateUser(User user) {
        if (userStorage.getUsers().stream()
                .anyMatch(u -> (u.getEmail().equals(user.getEmail()) && u.getId() != user.getId()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Юзер с имейлом " + user.getEmail() + "уже существует");
        }
        return userStorage.updateUser(user);
    }

    @Override
    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    @Override
    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    @Override
    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }
}

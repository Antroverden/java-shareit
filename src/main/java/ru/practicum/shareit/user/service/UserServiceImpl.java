package ru.practicum.shareit.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Override
    public User addUser(User user) {
        try {
            return userRepository.save(user);
        } catch (DataAccessException e) {
            throw new ResponseStatusException(CONFLICT,
                    "Юзер с имейлом " + user.getEmail() + "уже существует");
        }
    }

    @Override
    public User updateUser(User user) {
        User userFromDB = getUserById(user.getId());
        if (user.getName() != null) {
            userFromDB.setName(user.getName());
        }
        if (user.getEmail() != null) {
            userFromDB.setEmail(user.getEmail());
        }
        return userRepository.save(userFromDB);
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
                "Юзера с айди " + id + "не существует"));
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}

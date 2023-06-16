package ru.practicum.shareit.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

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
            throw new ConflictException("Юзер с имейлом " + user.getEmail() + "уже существует");
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
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(
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

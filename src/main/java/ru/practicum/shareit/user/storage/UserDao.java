package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class UserDao implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 0;

    @Override
    public User addUser(User user) {
        user.setId(++id);
        users.put(id, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getEmail() != null) {
            users.get(user.getId()).setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            users.get(user.getId()).setName(user.getName());
        }
        return users.get(user.getId());
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(int id) {
        return users.get(id);
    }

    @Override
    public void deleteUser(int id) {
        users.remove(id);
    }
}

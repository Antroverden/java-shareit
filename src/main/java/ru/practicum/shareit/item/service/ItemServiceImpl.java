package ru.practicum.shareit.item.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemServiceImpl implements ItemService {

    ItemStorage itemStorage;
    UserStorage userStorage;

    @Override
    public Item addItemToUser(Item item) {
        if (userStorage.getUserById(item.getOwnerId()) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Юзера с айди " + item.getOwnerId() + "не существует");
        }
        return itemStorage.addItem(item);
    }

    @Override
    public Item updateItem(Item item) {
        if (itemStorage.getItemById(item.getId()).getOwnerId() != item.getOwnerId()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Юзер с айди " + item.getOwnerId() + "не является владельцем данной вещи");
        }
        return itemStorage.updateItem(item);
    }

    @Override
    public Item getItemById(int id) {
        return itemStorage.getItemById(id);
    }

    @Override
    public List<Item> getItems(int ownerId) {
        return itemStorage.getItems().stream().filter(item -> item.getOwnerId() == ownerId)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteItem(int id) {
        itemStorage.deleteItem(id);
    }

    @Override
    public List<Item> searchItems(String text) {
        if (text.isBlank()) return List.of();
        return itemStorage.getItems().stream()
                .filter(item -> ((item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase())) && item.getAvailable()))
                .collect(Collectors.toList());
    }
}

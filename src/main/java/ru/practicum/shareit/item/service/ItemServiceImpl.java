package ru.practicum.shareit.item.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemServiceImpl implements ItemService {

    ItemRepository itemRepository;
    UserRepository userRepository;

    @Override
    public Item addItemToUser(Item item) {
        Integer ownerId = item.getOwner().getId();
        if (!userRepository.existsById(ownerId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Юзера с айди " + ownerId + "не существует");
        }
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(Item item) {
        Integer ownerFromDBId = itemRepository.findById(item.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Айтема с айди " + item.getId() + "не существует")).getId();
        Integer ownerId = item.getOwner().getId();
        Item itemFromDB = getItemById(item.getId());
        if (item.getAvailable() != null) {
            itemFromDB.setAvailable(item.getAvailable());
        }
        else if (!ownerFromDBId.equals(ownerId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Юзер с айди " + ownerId + "не является владельцем данной вещи");
        }
        if (item.getName() != null) {
            itemFromDB.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemFromDB.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemFromDB.setAvailable(item.getAvailable());
        }
        return itemRepository.save(itemFromDB);
    }

    @Override
    public Item getItemById(int id) {
        return itemRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Айтема с айди " + id + "не существует"));
    }

    @Override
    public List<Item> getItems(int ownerId) {
        return itemRepository.findAll().stream().filter(item -> item.getOwner().getId().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteItem(int id) {
        itemRepository.deleteById(id);
    }

    @Override
    public List<Item> searchItems(String text) {
        if (text.isBlank()) return List.of();
        return itemRepository.findAll().stream()
                .filter(item -> ((item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase())) && item.getAvailable()))
                .collect(Collectors.toList());
    }
}

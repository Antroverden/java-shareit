package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item addItemToUser(Item item);

    Item updateItem(Item item);

    Item getItemById(int id);

    List<Item> getItems(int ownerId);

    void deleteItem(int id);

    List<Item> searchItems(String text);
}

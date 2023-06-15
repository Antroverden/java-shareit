package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item addItemToUser(Item item);

    Item updateItem(Item item);

    Item getItemById(Integer id);

    ItemDto getItemById(Integer id, Integer userId);

    List<Item> getItems(int ownerId);

    void deleteItem(int id);

    List<Item> searchItems(String text);

    public Comment addCommentToItem(Comment comment);
}

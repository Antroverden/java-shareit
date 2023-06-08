package ru.practicum.shareit.item.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemServiceImpl implements ItemService {

    ItemStorage itemStorage;
    ItemMapper itemMapper;

    @Override
    public ItemDto addItem(ItemDto itemDto, int ownerId) {
        itemDto.setOwnerId(ownerId);
        return itemMapper.apply(itemStorage.addItem(itemMapper.toItem(itemDto)));
    }

    @Override
    public ItemDto updateItem(int id, ItemDto itemDto, int ownerId) {
        if (itemStorage.getItemById(id).getOwnerId() != ownerId) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Данный юзер не является владельцем данной вещи");
        }
        itemDto.setId(id);
        return itemMapper.apply(itemStorage.updateItem(itemMapper.toItem(itemDto)));
    }

    @Override
    public ItemDto getItemById(int id) {
        return itemMapper.apply(itemStorage.getItemById(id));
    }

    @Override
    public List<ItemDto> getItems(int ownerId) {
        return itemStorage.getItems().stream().filter(item -> item.getOwnerId() == ownerId).map(itemMapper)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteItem(int id) {
        itemStorage.deleteItem(id);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        return itemStorage.getItems().stream()
                .filter(item -> (item.getName().contains(text) || item.getDescription().contains(text)))
                .map(itemMapper).collect(Collectors.toList());
    }
}

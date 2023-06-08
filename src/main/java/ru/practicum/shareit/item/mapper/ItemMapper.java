package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.function.Function;

@Service
public class ItemMapper implements Function<Item, ItemDto> {
    @Override
    public ItemDto apply(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), item.getOwner(),
                item.getItemRequest());
    }

    public Item toItem(ItemDto itemDto) {
        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(),
                itemDto.getOwner(), itemDto.getItemRequest());
    }
}

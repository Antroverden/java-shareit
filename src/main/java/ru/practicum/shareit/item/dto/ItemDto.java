package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.ItemRequest;

@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    int id;
    String name;
    String description;
    Boolean available;
    String owner;
    ItemRequest itemRequest;
}

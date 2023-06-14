package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ItemMapper {

    UserService userService;
    ItemRequestRepository itemRequestRepository;

    public ItemDto toDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                item.getOwner().getId(), item.getItemRequest() == null ? null : item.getItemRequest().getId());
    }

    public Item toItem(ItemDto itemDto) {
        User user = userService.getUserById(itemDto.getOwnerId());
        ItemRequest itemRequest;
        if (itemDto.getRequestId() != null) {
            itemRequest = itemRequestRepository.findById(itemDto.getRequestId()).orElse(null);
        } else itemRequest = null;
        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(),
                user, itemRequest);
    }
}

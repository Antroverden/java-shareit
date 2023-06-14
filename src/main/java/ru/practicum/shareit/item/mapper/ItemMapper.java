package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ItemMapper {

    UserRepository userRepository;
    ItemRequestRepository itemRequestRepository;

    public ItemDto toDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                item.getOwner().getId(), item.getItemRequest().getId());
    }

    public Item toItem(ItemDto itemDto) {
        User user = userRepository.findById(itemDto.getOwnerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Юзера с айди " + itemDto.getOwnerId() + "не существует"));
        ItemRequest itemRequest = itemRequestRepository.findById(itemDto.getRequestId()).orElse(null);
        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(),
                user, itemRequest);
    }
}

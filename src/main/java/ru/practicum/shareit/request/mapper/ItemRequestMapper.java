package ru.practicum.shareit.request.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ItemRequestMapper {

    UserService userService;

    public ItemRequestDto toDto(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getId(), itemRequest.getRequestor().getId(), itemRequest.getDescription(), itemRequest.getCreated());
    }

    public List<ItemRequestDto> toDtos(List<ItemRequest> itemRequest) {
        return itemRequest.stream().map(this::toDto).collect(Collectors.toList());
    }

    public ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        User user = userService.getUserById(itemRequestDto.getRequestorId());
        return new ItemRequest(itemRequestDto.getId(), itemRequestDto.getDescription(), user, LocalDateTime.now());
    }
}

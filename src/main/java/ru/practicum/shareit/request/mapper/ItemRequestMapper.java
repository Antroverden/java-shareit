package ru.practicum.shareit.request.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ItemRequestMapper {

    UserService userService;
    ItemRepository itemRepository;
    ItemMapper itemMapper;

    public ItemRequestDto toDto(ItemRequest itemRequest) {
        List<ItemDto> itemDtos = itemMapper.toDto(itemRepository.findItemsByItemRequest_Id(itemRequest.getId()));
        return ItemRequestDto.builder().id(itemRequest.getId()).requestorId(itemRequest.getRequestor().getId())
                .description(itemRequest.getDescription()).created(itemRequest.getCreated()).items(itemDtos).build();
    }

    public List<ItemRequestDto> toDtos(List<ItemRequest> itemRequest) {
        return itemRequest.stream().map(this::toDto).collect(Collectors.toList());
    }

    public ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        User user = userService.getUserById(itemRequestDto.getRequestorId());
        return ItemRequest.builder().id(itemRequestDto.getId()).description(itemRequestDto.getDescription())
                .requestor(user).created(LocalDateTime.now()).build();
    }
}

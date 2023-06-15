package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemController {

    ItemService itemService;
    ItemMapper itemMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addItemToUser(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int ownerId) {
        itemDto.setOwnerId(ownerId);
        return itemMapper.toDto(itemService.addItemToUser(itemMapper.toItem(itemDto)));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@Valid @PathVariable int itemId, @RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") int ownerId) {
        itemDto.setId(itemId);
        itemDto.setOwnerId(ownerId);
        return itemMapper.toDto(itemService.updateItem(itemMapper.toItem(itemDto)));
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemService.getItems(ownerId).stream().map(itemMapper::toDtoWithLastAndNextBooking).collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        return itemService.searchItems(text).stream().map(itemMapper::toDto).collect(Collectors.toList());
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addCommentToItem(@Valid @RequestBody ItemDto itemDto, @PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int ownerId) {
        itemDto.setOwnerId(ownerId);
        return itemMapper.toDto(itemService.addItemToUser(itemMapper.toItem(itemDto)));
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable int itemId) {
        itemService.deleteItem(itemId);
    }
}

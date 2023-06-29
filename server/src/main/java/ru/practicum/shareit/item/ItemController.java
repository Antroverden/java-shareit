package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoWithName;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemController {

    ItemService itemService;
    ItemMapper itemMapper;
    CommentMapper commentMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addItemToUser(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int ownerId) {
        itemDto.setOwnerId(ownerId);
        return itemMapper.toDto(itemService.addItemToUser(itemMapper.toItem(itemDto)));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable int itemId, @RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") int ownerId) {
        itemDto.setId(itemId);
        itemDto.setOwnerId(ownerId);
        return itemMapper.toDto(itemService.updateItem(itemMapper.toItem(itemDto)));
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int userId) {
        Item item = itemService.getItemById(itemId);
        if (item.getOwner().getId().equals(userId)) {
            return itemMapper.toDtoWithLastAndNextBooking(item);
        }
        return itemMapper.toDto(item);
    }

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemMapper.toDto(itemService.getItems(ownerId));
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        return itemMapper.toDto(itemService.searchItems(text));
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoWithName addCommentToItem(@RequestBody CommentDto commentDto, @PathVariable int itemId,
                                               @RequestHeader("X-Sharer-User-Id") int userId) {
        commentDto.setItemId(itemId);
        commentDto.setAuthorId(userId);
        return commentMapper.toDto(itemService.addCommentToItem(commentMapper.toComment(commentDto)));
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable int itemId) {
        itemService.deleteItem(itemId);
    }
}

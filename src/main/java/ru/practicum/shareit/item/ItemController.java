package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemController {

    ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addItem(@RequestBody ItemDto itemDto) {
        return itemService.addItem(itemDto);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@PathVariable int id, @RequestBody ItemDto itemDto) {
        return itemService.updateItem(id, itemDto);
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable int id) {
        return itemService.getItemById(id);
    }

    @GetMapping
    public List<ItemDto> getItems() {
        return itemService.getItems();
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable int id) {
        itemService.deleteItem(id);
    }
}

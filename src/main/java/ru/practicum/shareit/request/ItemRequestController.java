package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class ItemRequestController {

    ItemRequestServiceImpl itemRequestService;
    ItemRequestMapper itemRequestMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto addItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                         @RequestHeader("X-Sharer-User-Id") int userId) {
        itemRequestDto.setRequestorId(userId);
        return itemRequestMapper.toDto(itemRequestService.addItemRequest(itemRequestMapper.toItemRequest(itemRequestDto)));
    }

    @GetMapping
    public List<ItemRequestDto> getItemRequestsForUser(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemRequestMapper.toDtos(itemRequestService.getItemRequestsForUser(userId));
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getItemRequests(@RequestHeader("X-Sharer-User-Id") int userId,
                                                @RequestParam(value = "from", required = false)
                                                @Min(value = 0) Integer from,
                                                @RequestParam(value = "size", required = false)
                                                @Min(value = 1) Integer size) {
        return itemRequestMapper.toDtos(itemRequestService.getItemRequests(userId, from, size));
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(@PathVariable int requestId, @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemRequestMapper.toDto(itemRequestService.getItemRequestById(requestId, userId));
    }

//    @PatchMapping("/{itemId}")
//    public ItemRequestDto updateItem(@Valid @PathVariable int itemId, @RequestBody ItemRequestDto itemRequestDto,
//                                     @RequestHeader("X-Sharer-User-Id") int ownerId) {
//        itemRequestDto.setId(itemId);
//        return itemRequestMapper.toDto(itemRequestService.updateItem(itemRequestMapper.toItem(itemRequestDto)));
//    }
//
//
//
//
//    @GetMapping("/search")
//    public List<ItemRequestDto> searchItems(@RequestParam String text) {
//        return itemRequestMapper.toDto(itemRequestService.searchItems(text));
//    }
//
//    @DeleteMapping("/{itemId}")
//    public void deleteItem(@PathVariable int itemId) {
//        itemRequestService.deleteItem(itemId);
//    }

}

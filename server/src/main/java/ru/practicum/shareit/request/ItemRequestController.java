package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemRequestController {

    ItemRequestServiceImpl itemRequestService;
    ItemRequestMapper itemRequestMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto addItemRequest(@RequestBody ItemRequestDto itemRequestDto,
                                         @RequestHeader("X-Sharer-User-Id") int userId) {
        itemRequestDto.setRequestorId(userId);
        return itemRequestMapper.toDto(itemRequestService.addItemRequest(itemRequestMapper
                .toItemRequest(itemRequestDto)));
    }

    @GetMapping
    public List<ItemRequestDto> getItemRequestsForUser(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemRequestMapper.toDtos(itemRequestService.getItemRequestsForUser(userId));
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getItemRequests(@RequestHeader("X-Sharer-User-Id") int userId,
                                                @RequestParam(value = "from", required = false)
                                                Integer from,
                                                @RequestParam(value = "size", required = false)
                                                Integer size) {
        return itemRequestMapper.toDtos(itemRequestService.getItemRequests(userId, from, size));
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(@PathVariable int requestId,
                                             @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemRequestMapper.toDto(itemRequestService.getItemRequestById(requestId, userId));
    }
}

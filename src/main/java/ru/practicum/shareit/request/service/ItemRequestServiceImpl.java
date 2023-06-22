package ru.practicum.shareit.request.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemRequestServiceImpl {

    ItemRequestRepository itemRequestRepository;
    UserService userService;

    public ItemRequest addItemRequest(ItemRequest itemRequest) {
        userService.getUserById(itemRequest.getRequestor().getId());
        return itemRequestRepository.save(itemRequest);
    }

    public ItemRequest getItemRequestById(Integer requestId, Integer userId) {
        userService.getUserById(userId);
        return itemRequestRepository.findById(requestId).orElseThrow(() -> new NotFoundException(
                "Запроса с айди " + requestId + "не существует"));
    }

    public List<ItemRequest> getItemRequestsForUser(int userId) {
        userService.getUserById(userId);
        return itemRequestRepository.findAllByRequestor_Id(userId);
    }

    public List<ItemRequest> getItemRequests(Integer userId, Integer from, Integer size) {
        userService.getUserById(userId);
        if (from != null && size != null) {
            return itemRequestRepository.findAllByRequestor_IdNot(userId, PageRequest.of(from / size, size))
                    .getContent();
        }
        return itemRequestRepository.findAllByRequestor_Id(userId);
    }
}

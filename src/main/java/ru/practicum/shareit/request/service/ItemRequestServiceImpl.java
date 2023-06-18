package ru.practicum.shareit.request.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemRequestServiceImpl {

    ItemRequestRepository itemRequestRepository;
    UserRepository userRepository;

    public ItemRequest addItemRequest(ItemRequest itemRequest) {
        Integer ownerId = itemRequest.getRequestor().getId();
        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException("Юзера с айди " + ownerId + "не существует");
        }
        return itemRequestRepository.save(itemRequest);
    }

    public ItemRequest getItemRequestById(Integer requestId, Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Юзера с айди " + userId + "не существует");
        }
        return itemRequestRepository.findById(requestId).orElseThrow(() -> new NotFoundException(
                "Запроса с айди " + requestId + "не существует"));
    }

    public List<ItemRequest> getItemRequestsForUser(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Юзера с айди " + userId + "не существует");
        }
        return itemRequestRepository.findAllByRequestor_Id(userId);
    }

    public List<ItemRequest> getItemRequests(Integer userId, Integer from, Integer size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Юзера с айди " + userId + "не существует");
        }
        if (from != null && size != null) {
            return itemRequestRepository.findAllByRequestor_Id(userId, PageRequest.of(from, size)).getContent();
        }
        return itemRequestRepository.findAllByRequestor_Id(userId);
    }
}

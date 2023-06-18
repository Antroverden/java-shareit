package ru.practicum.shareit.request.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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


//    public Item updateItem(Item item) {
//        Item itemFromDB = getItemRequestById(item.getId());
//        Integer ownerFromDBId = itemFromDB.getOwner().getId();
//        Integer ownerId = item.getOwner().getId();
//        if (!ownerFromDBId.equals(ownerId)) {
//            throw new ConflictException("Юзер с айди " + ownerId + "не является владельцем данной вещи");
//        }
//        if (item.getName() != null) {
//            itemFromDB.setName(item.getName());
//        }
//        if (item.getDescription() != null) {
//            itemFromDB.setDescription(item.getDescription());
//        }
//        if (item.getAvailable() != null) {
//            itemFromDB.setAvailable(item.getAvailable());
//        }
//        return itemRepository.save(itemFromDB);
//    }


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
        return itemRequestRepository.findAll();
    }


//    public void deleteItem(int id) {
//        itemRepository.deleteById(id);
//    }
//
//
//    public List<Item> searchItems(String text) {
//        if (text.isBlank()) return List.of();
//        return itemRepository.search(text);
//    }
//
//
//    public Comment addCommentToItem(Comment comment) {
//        if (comment.getText().isBlank()) {
//            throw new BadRequestException("Комментарий не может быть пустым");
//        }
//        List<Booking> userBookings = bookingRepository.findAllByBooker_IdAndEndBeforeOrderByStartDesc(
//                comment.getAuthor().getId(), LocalDateTime.now());
//        if (userBookings.isEmpty()) throw new BadRequestException(
//                "Юзер с айди " + comment.getAuthor().getId() + " ранее не бронировал вещи");
//        return commentRepository.save(comment);
//    }
}

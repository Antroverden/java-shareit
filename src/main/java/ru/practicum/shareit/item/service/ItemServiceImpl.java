package ru.practicum.shareit.item.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemServiceImpl implements ItemService {

    ItemRepository itemRepository;
    UserRepository userRepository;
    CommentRepository commentRepository;
    BookingRepository bookingRepository;
    ItemMapper itemMapper;

    @Override
    public Item addItemToUser(Item item) {
        Integer ownerId = item.getOwner().getId();
        if (!userRepository.existsById(ownerId)) {
            throw new ResponseStatusException(NOT_FOUND, "Юзера с айди " + ownerId + "не существует");
        }
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(Item item) {
        Item itemFromDB = getItemById(item.getId());
        Integer ownerFromDBId = itemFromDB.getOwner().getId();
        Integer ownerId = item.getOwner().getId();
        if (!ownerFromDBId.equals(ownerId)) {
            throw new ResponseStatusException(CONFLICT,
                    "Юзер с айди " + ownerId + "не является владельцем данной вещи");
        }
        if (item.getName() != null) {
            itemFromDB.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemFromDB.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemFromDB.setAvailable(item.getAvailable());
        }
        return itemRepository.save(itemFromDB);
    }

    @Override
    public Item getItemById(Integer id) {
        return itemRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
                "Айтема с айди " + id + "не существует"));
    }

    @Override
    public ItemDto getItemById(Integer id, Integer userId) {
        Item item = getItemById(id);
        if (item.getOwner().getId().equals(userId)) {
            return itemMapper.toDtoWithLastAndNextBooking(item);
        }
        return itemMapper.toDto(item);
    }

    @Override
    public List<Item> getItems(int ownerId) {
        return itemRepository.findItemsByOwner_IdOrderById(ownerId);
    }

    @Override
    public void deleteItem(int id) {
        itemRepository.deleteById(id);
    }

    @Override
    public List<Item> searchItems(String text) {
        if (text.isBlank()) return List.of();
        return itemRepository.search(text);
    }

    @Override
    public Comment addCommentToItem(Comment comment) {
        if (comment.getText().isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "Комментарий не может быть пустым");
        }
        List<Booking> userBookings = bookingRepository.findAllByBooker_IdAndEndBeforeOrderByStartDesc(
                comment.getAuthor().getId(), LocalDateTime.now());
        if (userBookings.isEmpty()) throw new ResponseStatusException(BAD_REQUEST,
                "Юзер с айди " + comment.getAuthor().getId() + " ранее не бронировал вещи");
        return commentRepository.save(comment);
    }
}

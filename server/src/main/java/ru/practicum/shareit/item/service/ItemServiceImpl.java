package ru.practicum.shareit.item.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemServiceImpl implements ItemService {

    ItemRepository itemRepository;
    UserRepository userRepository;
    CommentRepository commentRepository;
    BookingRepository bookingRepository;

    @Override
    public Item addItemToUser(Item item) {
        Integer ownerId = item.getOwner().getId();
        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException("Юзера с айди " + ownerId + "не существует");
        }
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(Item item) {
        Item itemFromDB = getItemById(item.getId());
        Integer ownerFromDBId = itemFromDB.getOwner().getId();
        Integer ownerId = item.getOwner().getId();
        if (!ownerFromDBId.equals(ownerId)) {
            throw new ConflictException("Юзер с айди " + ownerId + "не является владельцем данной вещи");
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
        return itemRepository.findById(id).orElseThrow(() -> new NotFoundException(
                "Айтема с айди " + id + "не существует"));
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
        return itemRepository
                .findItemsByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(text, text);
    }

    @Override
    public Comment addCommentToItem(Comment comment) {
        if (comment.getText().isBlank()) {
            throw new BadRequestException("Комментарий не может быть пустым");
        }
        List<Booking> userBookings = bookingRepository.findAllByBooker_IdAndEndBeforeOrderByStartDesc(
                comment.getAuthor().getId(), LocalDateTime.now());
        if (userBookings.isEmpty()) throw new BadRequestException(
                "Юзер с айди " + comment.getAuthor().getId() + " ранее не бронировал вещи");
        return commentRepository.save(comment);
    }
}

package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.LightBooking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDtoWithName;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ItemMapper {

    UserService userService;
    ItemRequestRepository itemRequestRepository;
    BookingRepository bookingRepository;
    CommentRepository commentRepository;
    CommentMapper commentMapper;

    public ItemDto toDto(Item item) {
        List<CommentDtoWithName> comments = commentRepository.findAllByItem_Id(item.getId()).stream().map(commentMapper::toDto).collect(Collectors.toList());
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                item.getOwner().getId(), item.getItemRequest() == null ? null : item.getItemRequest().getId(), null, null, comments);
    }

    public ItemDto toDtoWithLastAndNextBooking(Item item) {
        Booking lastBooking = bookingRepository.findFirstByStartBeforeAndItem_IdAndStatusInOrderByEndDesc(LocalDateTime.now(), item.getId(), List.of(Booking.Status.WAITING, Booking.Status.APPROVED));
        Booking nextBooking = bookingRepository.findFirstByStartAfterAndItem_IdAndStatusInOrderByStartAsc(LocalDateTime.now(), item.getId(), List.of(Booking.Status.WAITING, Booking.Status.APPROVED));
        LightBooking lastBook = null;
        LightBooking nextBook = null;
        if (lastBooking != null) {
            lastBook = new LightBooking(lastBooking.getId(), lastBooking.getBooker().getId());
        }
        if (nextBooking != null) {
            nextBook = new LightBooking(nextBooking.getId(), nextBooking.getBooker().getId());
        }
        List<CommentDtoWithName> comments = commentRepository.findAllByItem_Id(item.getId()).stream().map(commentMapper::toDto).collect(Collectors.toList());
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                item.getOwner().getId(), item.getItemRequest() == null ? null : item.getItemRequest().getId(), lastBook, nextBook, comments);
    }

    public Item toItem(ItemDto itemDto) {
        User user = userService.getUserById(itemDto.getOwnerId());
        ItemRequest itemRequest;
        if (itemDto.getRequestId() != null) {
            itemRequest = itemRequestRepository.findById(itemDto.getRequestId()).orElse(null);
        } else itemRequest = null;
        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(),
                user, itemRequest);
    }
}

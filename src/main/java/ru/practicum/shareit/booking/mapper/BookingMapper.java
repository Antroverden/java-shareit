package ru.practicum.shareit.booking.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoFull;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BookingMapper {

    UserService userService;
    ItemService itemService;

    public BookingDto toDto(Booking booking) {
        return new BookingDto(booking.getId(), booking.getStart(), booking.getEnd(), booking.getItem().getId(),
                booking.getBooker().getId(), booking.getStatus());
    }

    public BookingDtoFull toDtoFull(Booking booking) {
        return new BookingDtoFull(booking.getId(), booking.getStart(), booking.getEnd(), booking.getItem(),
                booking.getBooker(), booking.getStatus());
    }

    public Booking toBooking(BookingDto bookingDto) {
        Item item = bookingDto.getItemId() == null ? null : itemService.getItemById(bookingDto.getItemId());
        User booker = userService.getUserById(bookingDto.getBookerId());
        return new Booking(bookingDto.getId(), bookingDto.getStart(), bookingDto.getEnd(), item,
                booker, bookingDto.getStatus());
    }
}

package ru.practicum.shareit.booking.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithIdsFullDto;
import ru.practicum.shareit.booking.model.Booking;
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
        return BookingDto.builder().id(booking.getId()).start(booking.getStart()).end(booking.getEnd())
                .item(booking.getItem()).booker(booking.getBooker()).status(booking.getStatus()).build();
    }

    public Booking toBooking(BookingWithIdsFullDto bookingWithIdsFullDto) {
        Item item = bookingWithIdsFullDto.getItemId() == null ?
                null : itemService.getItemById(bookingWithIdsFullDto.getItemId());
        User booker = userService.getUserById(bookingWithIdsFullDto.getBookerId());
        return Booking.builder().id(bookingWithIdsFullDto.getId()).start(bookingWithIdsFullDto.getStart())
                .end(bookingWithIdsFullDto.getEnd()).item(item).booker(booker).status(bookingWithIdsFullDto.getStatus())
                .build();
    }
}

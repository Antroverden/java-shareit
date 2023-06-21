package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithIdsFullDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingMapperTest {

    @Mock
    private UserService userService;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private BookingMapper bookingMapper;

    @Test
    void toDto() {
        Booking booking = new Booking();
        booking.setId(1);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);

        booking.setStart(now);
        booking.setEnd(tomorrow);

        BookingDto bookingDto = bookingMapper.toDto(booking);

        assertThat(bookingDto).isNotNull();

        assertThat(booking.getId()).isEqualTo(bookingDto.getId());
        assertThat(booking.getStart()).isEqualTo(bookingDto.getStart());
        assertThat(booking.getEnd()).isEqualTo(bookingDto.getEnd());
    }

    @Test
    void testToDto() {
        Booking booking = Booking.builder().id(1).build();

        List<BookingDto> result = bookingMapper.toDto(List.of(booking));

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getId()).isEqualTo(booking.getId());
    }

    @Test
    void toBooking() {
        User user = new User(1, "Name", "email@rambler.ru");
        ItemRequest itemRequest = new ItemRequest();
        Item item = new Item(1, "itemName", "ItemDescription", true, user, itemRequest);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);

        BookingWithIdsFullDto bookingWithIdsFullDto = BookingWithIdsFullDto.builder().id(1).bookerId(1).itemId(1)
                .start(now).end(tomorrow).status(Status.WAITING).build();

        when(userService.getUserById(bookingWithIdsFullDto.getBookerId())).thenReturn(user);
        when(itemService.getItemById(bookingWithIdsFullDto.getItemId())).thenReturn(item);

        Booking result = bookingMapper.toBooking(bookingWithIdsFullDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(bookingWithIdsFullDto.getId());
        assertThat(result.getItem()).isNotNull();
        assertThat(result.getBooker()).isNotNull();
    }
}
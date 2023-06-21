package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.booking.model.Status.APPROVED;
import static ru.practicum.shareit.booking.model.Status.WAITING;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @InjectMocks
    BookingServiceImpl bookingService;

    @Mock
    BookingRepository bookingRepository;

    @Mock
    ItemService itemService;

    @Mock
    UserService userService;

    Booking booking;
    Item item;
    User owner;
    User booker;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(1);

        booker = new User();
        booker.setId(2);

        item = new Item();
        item.setId(1);
        item.setOwner(owner);
        item.setAvailable(true);

        booking = new Booking();
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));
    }

    @Test
    void add() {
        when(itemService.getItemById(item.getId())).thenReturn(item);
        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking result = bookingService.add(booking);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(WAITING);
        verify(bookingRepository).save(booking);
    }

    @Test
    void addUserAsOwnBookerException() {
        item.setOwner(owner);
        booking.setBooker(owner);

        assertThrows(NotFoundException.class, () -> {
            bookingService.add(booking);
        }, "Юзер с айди" + owner.getId() + " является владельцем данной вещи");
    }

    @Test
    void addBookingWithInvalidDatesException() {
        booking.setEnd(booking.getStart().minusDays(1));

        assertThrows(BadRequestException.class, () -> {
            bookingService.add(booking);
        }, "Вещь недоступна");
    }

    @Test
    void addUserItemUnavailable() {
        item.setOwner(owner);
        booking.setBooker(booker);
        item.setAvailable(false);
        when(itemService.getItemById(booking.getItem().getId())).thenReturn(item);

        assertThrows(BadRequestException.class, () -> {
            bookingService.add(booking);
        }, "Вещь недоступна");
    }

    @Test
    void testUpdateBookingSuccessfully() {
        booking.setStatus(WAITING);
        Booking updatedBooking = new Booking();

        updatedBooking.setBooker(booker);
        updatedBooking.setItem(item);
        updatedBooking.setStart(LocalDateTime.now());
        updatedBooking.setEnd(LocalDateTime.now().plusDays(1));
        updatedBooking.setStatus(APPROVED);
        item.setOwner(booker);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        when(bookingRepository.save(booking)).thenReturn(updatedBooking);

        Booking result = bookingService.update(updatedBooking);

        assertThat(result.getStatus()).isEqualTo(APPROVED);
    }

    @Test
    void testUpdateBookingNotFound() {

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.update(booking));
    }

    @Test
    void testUpdateBookingBookerIsNotOwner() {

        booking.getItem().getOwner().setId(owner.getId());

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThrows(NotFoundException.class, () -> bookingService.update(booking));
    }

    @Test
    void testUpdateBookingAlreadyApproved() {
        booking.setStatus(APPROVED);
        Booking updatedBooking = new Booking();
        updatedBooking.setBooker(owner);
        updatedBooking.setItem(item);
        updatedBooking.setStart(LocalDateTime.now());
        updatedBooking.setEnd(LocalDateTime.now().plusDays(1));
        updatedBooking.setStatus(APPROVED);

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThrows(BadRequestException.class, () -> bookingService.update(updatedBooking));
    }

    @Test
    void testGetByIdBookingExistsAndUserIsOwnerOrBooker() {
        User user = new User(1, "Name", "email@rambler.ru");
        booking.getItem().setOwner(user);
        booking.setId(1);
        int userId = booking.getItem().getOwner().getId();

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        Booking result = bookingService.getById(booking.getId(), userId);
        assertThat(result).isEqualTo(booking);
    }

    @Test
    void testGetByIdBookingExistsAndUserIsNotOwnerOrBooker() {
        User user = new User(10, "Name", "email@rambler.ru");
        int userId = user.getId();
        booking.setBooker(booker);
        booking.setId(1);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThrows(NotFoundException.class, () -> bookingService.getById(booking.getId(), userId));
    }

    @Test
    void testGetByIdBookingNotFound() {
        int bookingId = 1;
        int userId = 1;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.getById(bookingId, userId));
    }

    @Test
    void testGetAllForOwnerAllBookings() {
        User user = new User(1, "Name", "email@rambler.ru");
        booking.setBooker(booker);
        booking.setId(1);
        List<Booking> bookings = List.of(booking);
        int userId = bookings.get(0).getItem().getOwner().getId();

        when(userService.getUserById(userId)).thenReturn(user);
        when(bookingRepository.findAllByItem_Owner_IdOrderByStartDesc(userId)).thenReturn(bookings);

        List<Booking> result = bookingService.getAll(userId, "ALL", true, null, null);
        assertThat(result).isEqualTo(bookings);
    }

    @Test
    void testGetAllForBookerAllBookings() {
        booking.setBooker(booker);
        booking.setId(1);
        List<Booking> bookings = List.of(booking);
        int userId = bookings.get(0).getBooker().getId();
        when(userService.getUserById(userId)).thenReturn(booker);
        when(bookingRepository.findAllByBooker_IdOrderByStartDesc(userId)).thenReturn(bookings);

        List<Booking> result = bookingService.getAll(userId, "ALL", false, null, null);
        assertThat(result).isEqualTo(bookings);
    }

    @Test
    void testGetAllForOwnerAllBookingsWithPagination() {
        User user = new User(1, "Name", "email@rambler.ru");
        booking.setBooker(booker);
        booking.setId(1);
        List<Booking> bookings = List.of(booking);
        booking.getItem().setOwner(user);
        int userId = bookings.get(0).getItem().getOwner().getId();
        int from = 0;
        int size = 5;
        when(userService.getUserById(userId)).thenReturn(user);
        when(bookingRepository.findAllByItem_Owner_IdOrderByStartDesc(userId, PageRequest.of(from / size, size))).thenReturn(new PageImpl<>(bookings));

        List<Booking> result = bookingService.getAll(userId, "ALL", true, from, size);
        assertThat(result).isEqualTo(bookings);
    }

    @Test
    void testGetAllForBookerAllBookingsWithPagination() {
        booking.setBooker(booker);
        booking.setId(1);
        List<Booking> bookings = List.of(booking);
        int userId = bookings.get(0).getBooker().getId();
        int from = 0;
        int size = 5;
        when(userService.getUserById(userId)).thenReturn(booker);
        when(bookingRepository.findAllByBooker_IdOrderByStartDesc(userId, PageRequest.of(from / size, size))).thenReturn(new PageImpl<>(bookings));

        List<Booking> result = bookingService.getAll(userId, "ALL", false, from, size);
        assertThat(result).isEqualTo(bookings);
    }

    @Test
    void testGetAllForOwnerWaitingBookings() {
        User user = new User(1, "Name", "email@rambler.ru");
        booking.setBooker(booker);
        booking.setId(1);
        List<Booking> bookings = List.of(booking);
        booking.getItem().setOwner(user);
        int userId = bookings.get(0).getItem().getOwner().getId();
        when(userService.getUserById(userId)).thenReturn(user);
        when(bookingRepository.findAllByItem_Owner_IdAndStatusOrderByStartDesc(userId, WAITING)).thenReturn(bookings);

        List<Booking> result = bookingService.getAll(userId, "WAITING", true, null, null);
        assertThat(result).isEqualTo(bookings);
    }

    @Test
    void testGetAllForBookerWaitingBookings() {
        booking.setBooker(booker);
        booking.setId(1);
        List<Booking> bookings = List.of(booking);
        int userId = bookings.get(0).getBooker().getId();
        when(userService.getUserById(userId)).thenReturn(booker);
        when(bookingRepository.findAllByBooker_IdAndStatusOrderByStartDesc(userId, WAITING)).thenReturn(bookings);

        List<Booking> result = bookingService.getAll(userId, "WAITING", false, null, null);
        assertThat(result).isEqualTo(bookings);
    }
}
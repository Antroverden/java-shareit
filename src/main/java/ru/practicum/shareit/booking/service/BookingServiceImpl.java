package ru.practicum.shareit.booking.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingController.State;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl {

    BookingRepository bookingRepository;
    ItemService itemService;
    UserService userService;

    public Booking addBooking(Booking booking) {
        Integer ownerId = booking.getItem().getOwner().getId();
        if (booking.getBooker().getId().equals(booking.getItem().getOwner().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Юзер с айди " + ownerId + "является владельцем данной вещи");
        }
        if (booking.getEnd().isBefore(booking.getStart()) || booking.getStart().isEqual(booking.getEnd())
                || !itemService.getItemById(booking.getItem().getId()).getAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        booking.setStatus(Booking.Status.WAITING);
        return bookingRepository.save(booking);
    }

    public Booking updateBooking(Booking booking) {
        Booking bookingFromDB = bookingRepository.findById(booking.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Integer ownerId = bookingFromDB.getItem().getOwner().getId();
        if (!(booking.getBooker().getId().equals(ownerId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Юзер с айди " + ownerId + "не является владельцем данной вещи");
        }
        if (booking.getStatus() != null && booking.getStatus() == Booking.Status.APPROVED && bookingFromDB.getStatus() == Booking.Status.APPROVED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Юзер с айди " + ownerId + "уже одобрил бронирование данной вещи");
        }
        bookingFromDB.setStatus(booking.getStatus());
        return bookingRepository.save(bookingFromDB);
    }

    public Booking getBookingById(int id, Integer userId) {
        Booking bookingFromDB = bookingRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Бронирования с айди " + id + "не существует"));
        Integer ownerId = bookingFromDB.getItem().getOwner().getId();
        Integer bookerId = bookingFromDB.getBooker().getId();
        if (!(userId.equals(ownerId) || userId.equals(bookerId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Юзер с айди " + ownerId + "не является владельцем или автором бронирования данной вещи");
        }
        return bookingFromDB;
    }

    public List<Booking> getBookings(int userId, State state, boolean getForOwner) {
        userService.getUserById(userId);
        switch (state) {
            case ALL: {
                if (getForOwner) return bookingRepository.findAllByItem_Owner_IdOrderByStartDesc(userId);
                else return bookingRepository.findAllByBooker_IdOrderByStartDesc(userId);
            }
            case WAITING: {
                if (getForOwner)
                    return bookingRepository.findAllByItem_Owner_IdAndStatusOrderByStartDesc(userId, Booking.Status.WAITING);
                else
                    return bookingRepository.findAllByBooker_IdAndStatusOrderByStartDesc(userId, Booking.Status.WAITING);
            }
            case REJECTED: {
                if (getForOwner)
                    return bookingRepository.findAllByItem_Owner_IdAndStatusOrderByStartDesc(userId, Booking.Status.REJECTED);
                else
                    return bookingRepository.findAllByBooker_IdAndStatusOrderByStartDesc(userId, Booking.Status.REJECTED);
            }
            case PAST:
            case FUTURE: {
                if (getForOwner) {
                    return bookingRepository.findAllByItem_Owner_IdAndStartAfterOrderByStartDesc(userId,
                            LocalDateTime.now());
                } else {
                    return bookingRepository.findAllByBooker_IdAndStartAfterOrderByStartDesc(userId,
                            LocalDateTime.now());
                }
            }
            case CURRENT:
            default:
                return null;
        }
    }
}

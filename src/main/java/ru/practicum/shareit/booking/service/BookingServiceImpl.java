package ru.practicum.shareit.booking.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.practicum.shareit.booking.Booking.Status.REJECTED;
import static ru.practicum.shareit.booking.Booking.Status.WAITING;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl implements BookingService {

    BookingRepository bookingRepository;
    ItemService itemService;
    UserService userService;

    @Override
    public Booking addBooking(Booking booking) {
        Integer ownerId = booking.getItem().getOwner().getId();
        if (booking.getBooker().getId().equals(booking.getItem().getOwner().getId())) {
            throw new ResponseStatusException(NOT_FOUND,
                    "Юзер с айди " + ownerId + "является владельцем данной вещи");
        }
        if (booking.getEnd().isBefore(booking.getStart()) || booking.getStart().isEqual(booking.getEnd())
                || !itemService.getItemById(booking.getItem().getId()).getAvailable()) {
            throw new ResponseStatusException(BAD_REQUEST);
        }
        booking.setStatus(WAITING);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateBooking(Booking booking) {
        Booking bookingFromDB = bookingRepository.findById(booking.getId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        Integer ownerId = bookingFromDB.getItem().getOwner().getId();
        if (!(booking.getBooker().getId().equals(ownerId))) {
            throw new ResponseStatusException(NOT_FOUND,
                    "Юзер с айди " + ownerId + "не является владельцем данной вещи");
        }
        if (booking.getStatus() != null && booking.getStatus() == Booking.Status.APPROVED
                && bookingFromDB.getStatus() == Booking.Status.APPROVED) {
            throw new ResponseStatusException(BAD_REQUEST,
                    "Юзер с айди " + ownerId + "уже одобрил бронирование данной вещи");
        }
        bookingFromDB.setStatus(booking.getStatus());
        return bookingRepository.save(bookingFromDB);
    }

    @Override
    public Booking getBookingById(int id, Integer userId) {
        Booking bookingFromDB = bookingRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(NOT_FOUND, "Бронирования с айди " + id + "не существует"));
        Integer ownerId = bookingFromDB.getItem().getOwner().getId();
        Integer bookerId = bookingFromDB.getBooker().getId();
        if (!(userId.equals(ownerId) || userId.equals(bookerId))) {
            throw new ResponseStatusException(NOT_FOUND,
                    "Юзер с айди " + ownerId + "не является владельцем или автором бронирования данной вещи");
        }
        return bookingFromDB;
    }

    @Override
    public List<Booking> getBookings(int userId, State state, boolean getForOwner) {
        userService.getUserById(userId);
        LocalDateTime now;
        switch (state) {
            case ALL:
                if (getForOwner) return bookingRepository.findAllByItem_Owner_IdOrderByStartDesc(userId);
                else return bookingRepository.findAllByBooker_IdOrderByStartDesc(userId);
            case WAITING:
                if (getForOwner)
                    return bookingRepository.findAllByItem_Owner_IdAndStatusOrderByStartDesc(userId, WAITING);
                else
                    return bookingRepository.findAllByBooker_IdAndStatusOrderByStartDesc(userId, WAITING);
            case REJECTED:
                if (getForOwner)
                    return bookingRepository.findAllByItem_Owner_IdAndStatusOrderByStartDesc(userId, REJECTED);
                else
                    return bookingRepository.findAllByBooker_IdAndStatusOrderByStartDesc(userId, REJECTED);
            case PAST:
                now = LocalDateTime.now();
                if (getForOwner) {
                    return bookingRepository.findAllByItem_Owner_IdAndEndBeforeOrderByStartDesc(userId, now);
                } else {
                    return bookingRepository.findAllByBooker_IdAndEndBeforeOrderByStartDesc(userId, now);
                }
            case FUTURE:
                now = LocalDateTime.now();
                if (getForOwner) {
                    return bookingRepository.findAllByItem_Owner_IdAndStartAfterOrderByStartDesc(userId, now);
                } else {
                    return bookingRepository.findAllByBooker_IdAndStartAfterOrderByStartDesc(userId, now);
                }
            case CURRENT:
                now = LocalDateTime.now();
                if (getForOwner) {
                    return bookingRepository.findAllByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                            now, now);
                } else {
                    return bookingRepository.findAllByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                            now, now);
                }
            default:
                return null;
        }
    }

    public enum State {
        ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED
    }
}

package ru.practicum.shareit.booking.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.booking.model.Status.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl implements BookingService {

    BookingRepository bookingRepository;
    ItemService itemService;
    UserService userService;

    @Override
    public Booking add(Booking booking) {
        Integer ownerId = booking.getItem().getOwner().getId();
        if (booking.getBooker().getId().equals(booking.getItem().getOwner().getId())) {
            throw new NotFoundException("Юзер с айди " + ownerId + "является владельцем данной вещи");
        }
        if (booking.getEnd().isBefore(booking.getStart()) || booking.getStart().isEqual(booking.getEnd())
                || !itemService.getItemById(booking.getItem().getId()).getAvailable()) {
            throw new BadRequestException("Вещь недоступна");
        }
        booking.setStatus(WAITING);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking update(Booking booking) {
        Booking bookingFromDB = bookingRepository.findById(booking.getId()).orElseThrow(
                () -> new NotFoundException("Бронирования с айди " + booking.getId() + "не существует"));
        Integer ownerId = bookingFromDB.getItem().getOwner().getId();
        if (!(booking.getBooker().getId().equals(ownerId))) {
            throw new NotFoundException("Юзер с айди " + ownerId + "не является владельцем данной вещи");
        }
        if (booking.getStatus() != null && booking.getStatus() == APPROVED && bookingFromDB.getStatus() == APPROVED) {
            throw new BadRequestException("Юзер с айди " + ownerId + "уже одобрил бронирование данной вещи");
        }
        bookingFromDB.setStatus(booking.getStatus());
        return bookingRepository.save(bookingFromDB);
    }

    @Override
    public Booking getById(int id, Integer userId) {
        Booking bookingFromDB = bookingRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Бронирования с айди " + id + "не существует"));
        Integer ownerId = bookingFromDB.getItem().getOwner().getId();
        Integer bookerId = bookingFromDB.getBooker().getId();
        if (!(userId.equals(ownerId) || userId.equals(bookerId))) {
            throw new NotFoundException(
                    "Юзер с айди " + ownerId + "не является владельцем или автором бронирования данной вещи");
        }
        return bookingFromDB;
    }

    @Override
    public List<Booking> getAll(int userId, String state, boolean getForOwner, Integer from, Integer size) {
        userService.getUserById(userId);
        LocalDateTime now;
        switch (state) {
            case "ALL":
                if (getForOwner) {
                    if (from != null && size != null) {
                        return bookingRepository.findAllByItem_Owner_IdOrderByStartDesc(userId,
                                PageRequest.of(from / size, size)).getContent();
                    }
                    return bookingRepository.findAllByItem_Owner_IdOrderByStartDesc(userId);
                } else {
                    if (from != null && size != null) {
                        return bookingRepository.findAllByBooker_IdOrderByStartDesc(userId,
                                PageRequest.of(from / size, size)).getContent();
                    }
                    return bookingRepository.findAllByBooker_IdOrderByStartDesc(userId);
                }
            case "WAITING":
                if (getForOwner) {
                    return bookingRepository.findAllByItem_Owner_IdAndStatusOrderByStartDesc(userId, WAITING);
                } else {
                    return bookingRepository.findAllByBooker_IdAndStatusOrderByStartDesc(userId, WAITING);
                }
            case "REJECTED":
                if (getForOwner) {
                    return bookingRepository.findAllByItem_Owner_IdAndStatusOrderByStartDesc(userId, REJECTED);
                } else {
                    return bookingRepository.findAllByBooker_IdAndStatusOrderByStartDesc(userId, REJECTED);
                }
            case "PAST":
                now = LocalDateTime.now();
                if (getForOwner) {
                    return bookingRepository.findAllByItem_Owner_IdAndEndBeforeOrderByStartDesc(userId, now);
                } else {
                    return bookingRepository.findAllByBooker_IdAndEndBeforeOrderByStartDesc(userId, now);
                }
            case "FUTURE":
                now = LocalDateTime.now();
                if (getForOwner) {
                    return bookingRepository.findAllByItem_Owner_IdAndStartAfterOrderByStartDesc(userId, now);
                } else {
                    return bookingRepository.findAllByBooker_IdAndStartAfterOrderByStartDesc(userId, now);
                }
            case "CURRENT":
                now = LocalDateTime.now();
                if (getForOwner) {
                    return bookingRepository.findAllByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                            now, now);
                } else {
                    return bookingRepository.findAllByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                            now, now);
                }
            default:
                throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}

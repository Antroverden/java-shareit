package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithIdsFullDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

import static ru.practicum.shareit.booking.model.Status.APPROVED;
import static ru.practicum.shareit.booking.model.Status.REJECTED;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {

    BookingService bookingService;
    BookingMapper bookingMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto addBooking(@RequestBody BookingWithIdsFullDto bookingWithIdsFullDto,
                                 @RequestHeader("X-Sharer-User-Id") int bookerId) {
        bookingWithIdsFullDto.setBookerId(bookerId);
        return bookingMapper.toDto(bookingService.add(bookingMapper.toBooking(bookingWithIdsFullDto)));
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveOrRejectBooking(@PathVariable int bookingId, @RequestParam boolean approved,
                                             @RequestHeader("X-Sharer-User-Id") int bookerId) {
        BookingWithIdsFullDto bookingWithIdsFullDto = new BookingWithIdsFullDto();
        bookingWithIdsFullDto.setId(bookingId);
        bookingWithIdsFullDto.setBookerId(bookerId);
        if (approved) bookingWithIdsFullDto.setStatus(APPROVED);
        else bookingWithIdsFullDto.setStatus(REJECTED);
        return bookingMapper.toDto(bookingService.update(bookingMapper.toBooking(bookingWithIdsFullDto)));
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingInfo(@PathVariable int bookingId, @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingMapper.toDto(bookingService.getById(bookingId, userId));
    }

    @GetMapping
    public List<BookingDto> getBookings(
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestHeader("X-Sharer-User-Id") int bookerId,
            @RequestParam(value = "from", required = false)
            Integer from,
            @RequestParam(value = "size", required = false)
            Integer size) {
        return bookingMapper.toDto(bookingService.getAll(bookerId, state, false, from, size));
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsWithInfoOfItems(
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestHeader("X-Sharer-User-Id") int ownerId,
            @RequestParam(value = "from", required = false)
            Integer from,
            @RequestParam(value = "size", required = false)
            Integer size) {
        return bookingMapper.toDto(bookingService.getAll(ownerId, state, true, from, size));
    }
}



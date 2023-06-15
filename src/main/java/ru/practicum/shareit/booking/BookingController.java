package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.Booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoFull;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {

    BookingService bookingService;
    BookingMapper bookingMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDtoFull addBooking(@Valid @RequestBody BookingDto bookingDto,
                                     @RequestHeader("X-Sharer-User-Id") int bookerId) {
        bookingDto.setBookerId(bookerId);
        return bookingMapper.toDtoFull(bookingService.addBooking(bookingMapper.toBooking(bookingDto)));
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoFull approveOrRejectBooking(@PathVariable int bookingId, @RequestParam boolean approved,
                                                 @RequestHeader("X-Sharer-User-Id") int bookerId) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(bookingId);
        bookingDto.setBookerId(bookerId);
        if (approved) bookingDto.setStatus(Status.APPROVED);
        else bookingDto.setStatus(Status.REJECTED);
        return bookingMapper.toDtoFull(bookingService.updateBooking((bookingMapper.toBooking(bookingDto))));
    }

    @GetMapping("/{bookingId}")
    public BookingDtoFull getBookingInfo(@PathVariable int bookingId, @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingMapper.toDtoFull(bookingService.getBookingById(bookingId, userId));
    }

    @GetMapping
    public List<BookingDtoFull> getBookingInfo(
            @RequestParam(required = false, defaultValue = "ALL") State state,
            @RequestHeader("X-Sharer-User-Id") int bookerId) {
        return Optional.ofNullable(bookingService.getBookings(bookerId, state, false)).stream().flatMap(Collection::stream)
                .map(bookingMapper::toDtoFull).collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingDtoFull> getBookingInfoOfItems(
            @RequestParam(required = false, defaultValue = "ALL") State state,
            @RequestHeader("X-Sharer-User-Id") int ownerId) {
        return Optional.ofNullable(bookingService.getBookings(ownerId, state, true)).stream().flatMap(Collection::stream)
                .map(bookingMapper::toDtoFull).collect(Collectors.toList());
    }

    public enum State {
        ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED
    }
}

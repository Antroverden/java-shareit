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
import ru.practicum.shareit.booking.service.BookingServiceImpl;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {

    BookingServiceImpl bookingService;
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
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(bookingId);
        return bookingMapper.toDtoFull(bookingService.getBookingById(bookingId, userId));
    }

    @GetMapping
    public List<BookingDtoFull> getBookingInfoOfBooking(
            @RequestParam(required = false, defaultValue = "ALL") State state,
            @RequestHeader("X-Sharer-User-Id") int bookerId) {
        return bookingService.getBookings(bookerId, state, false).stream().map(bookingMapper::toDtoFull).collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingDtoFull> getBookingInfoOfItemsOfBooking(
            @RequestParam(required = false, defaultValue = "ALL") State state,
            @RequestHeader("X-Sharer-User-Id") int ownerId) {
        return bookingService.getBookings(ownerId, state, true).stream().map(bookingMapper::toDtoFull).collect(Collectors.toList());
    }

    public enum State {
        ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED
    }
}

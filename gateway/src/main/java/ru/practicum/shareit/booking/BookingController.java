package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.BadRequestException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {

    BookingClient bookingClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveOrRejectBooking(@PathVariable int bookingId, @RequestParam boolean approved,
                                                         @RequestHeader("X-Sharer-User-Id") int bookerId) {
        return bookingClient.approveOrRejectBooking(bookingId, approved, bookerId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new BadRequestException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsWithInfoOfItems(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(name = "state", defaultValue = "all") String stateParam,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new BadRequestException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookingsForOwner(userId, state, from, size);
    }
}

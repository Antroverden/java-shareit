package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.booking.Booking.Status;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto addBooking(@Valid @RequestBody BookingDto bookingDto) {
        return userMapper.toDto(userService.addUser(userMapper.toUser(userDto)));
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveOrRejectBooking(@PathVariable int bookingId, @RequestParam boolean approved) {
        return userMapper.toDto(userService.addUser(userMapper.toUser(userDto)));
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingInfo(@PathVariable int bookingId) {
        return userMapper.toDto(userService.addUser(userMapper.toUser(userDto)));
    }

    @GetMapping
    public BookingDto getBookingInfoOfUser(@RequestParam(required = false, defaultValue = "ALL") State state) {
        return userMapper.toDto(userService.addUser(userMapper.toUser(userDto)));
    }

    @GetMapping("/owner")
    public BookingDto getBookingInfoOfItemsOfUser(@RequestParam(required = false, defaultValue = "ALL") State state) {
        return userMapper.toDto(userService.addUser(userMapper.toUser(userDto)));
    }

    enum State {
        ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED
    }
}

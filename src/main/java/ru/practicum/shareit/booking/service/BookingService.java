package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.Booking;

import java.util.List;

public interface BookingService {
    Booking addBooking(Booking booking);

    Booking updateBooking(Booking booking);

    Booking getBookingById(int id, Integer userId);

    List<Booking> getBookings(int userId, String state, boolean getForOwner);
}

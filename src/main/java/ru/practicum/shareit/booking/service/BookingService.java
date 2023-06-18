package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    Booking add(Booking booking);

    Booking update(Booking booking);

    Booking getById(int id, Integer userId);

    List<Booking> getAll(int userId, String state, boolean getForOwner, Integer from, Integer size);
}

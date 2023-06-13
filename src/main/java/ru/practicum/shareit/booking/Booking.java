package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {
    int id;
    LocalDate start;
    LocalDate end;
    Item item;
    User booker;
    Status status;

    private enum Status {
        WAITING, APPROVED, REJECTED, CANCELED
    }
}

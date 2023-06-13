package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

@Value
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequest {
    int id;
    String description;
    User requestor;
    LocalDate created;
}

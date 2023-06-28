package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingWithIdsDto;

import java.util.List;

@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    int id;
    String name;
    String description;
    Boolean available;
    int ownerId;
    Integer requestId;
    BookingWithIdsDto lastBooking;
    BookingWithIdsDto nextBooking;
    List<CommentDtoWithName> comments;
}

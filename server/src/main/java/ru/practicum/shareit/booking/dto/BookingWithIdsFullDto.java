package ru.practicum.shareit.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
public class BookingWithIdsFullDto {
    Integer id;
    LocalDateTime start;
    LocalDateTime end;
    Integer itemId;
    Integer bookerId;
    Status status;
}

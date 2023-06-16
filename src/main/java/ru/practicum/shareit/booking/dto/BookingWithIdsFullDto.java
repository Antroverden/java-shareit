package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import ru.practicum.shareit.booking.model.Status;

@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class BookingWithIdsFullDto {
    Integer id;
    @NotNull
    @FutureOrPresent
    LocalDateTime start;
    @NotNull
    @FutureOrPresent
    LocalDateTime end;
    Integer itemId;
    Integer bookerId;
    Status status;
}

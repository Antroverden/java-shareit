package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByItem_Owner_IdOrderByStartDesc(Integer ownerId);

    List<Booking> findAllByItem_Owner_IdAndStatusOrderByStartDesc(Integer ownerId, Booking.Status status);

    List<Booking> findAllByBooker_IdOrderByStartDesc(Integer bookerId);

    List<Booking> findAllByBooker_IdAndStatusOrderByStartDesc(Integer bookerId, Booking.Status status);

    List<Booking> findAllByItem_Owner_IdAndStartAfterOrderByStartDesc(Integer ownerId, LocalDateTime now);

    List<Booking> findAllByBooker_IdAndStartAfterOrderByStartDesc(Integer bookerId, LocalDateTime now);

    Booking findFirstByStartAfterAndItem_IdOrderByStartAsc(LocalDateTime now, Integer itemId);

    Booking findFirstByEndBeforeAndItem_IdOrderByEndDesc(LocalDateTime now, Integer itemId);
}

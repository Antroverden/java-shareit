package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByItem_Owner_IdOrderByStartDesc(Integer ownerId);

    Page<Booking> findAllByItem_Owner_IdOrderByStartDesc(Integer ownerId, Pageable pageable);

    List<Booking> findAllByItem_Owner_IdAndStatusOrderByStartDesc(Integer ownerId, Status status);

    List<Booking> findAllByBooker_IdOrderByStartDesc(Integer bookerId);

    Page<Booking> findAllByBooker_IdOrderByStartDesc(Integer ownerId, Pageable pageable);

    List<Booking> findAllByBooker_IdAndStatusOrderByStartDesc(Integer bookerId, Status status);

    List<Booking> findAllByItem_Owner_IdAndStartAfterOrderByStartDesc(Integer ownerId, LocalDateTime now);

    List<Booking> findAllByItem_Owner_IdAndEndBeforeOrderByStartDesc(Integer ownerId, LocalDateTime now);

    List<Booking> findAllByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(Integer ownerId, LocalDateTime now,
                                                                                  LocalDateTime now1);

    List<Booking> findAllByBooker_IdAndStartAfterOrderByStartDesc(Integer bookerId, LocalDateTime now);

    List<Booking> findAllByBooker_IdAndEndBeforeOrderByStartDesc(Integer bookerId, LocalDateTime now);

    List<Booking> findAllByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(Integer bookerId, LocalDateTime now,
                                                                              LocalDateTime now1);

    Booking findFirstByStartAfterAndItem_IdAndStatusInOrderByStartAsc(LocalDateTime now, Integer itemId,
                                                                      List<Status> statuses);

    Booking findFirstByStartBeforeAndItem_IdAndStatusInOrderByEndDesc(LocalDateTime now, Integer itemId,
                                                                      List<Status> statuses);
}

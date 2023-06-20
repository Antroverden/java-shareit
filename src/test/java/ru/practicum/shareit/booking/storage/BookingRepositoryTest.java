package ru.practicum.shareit.booking.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
    }

    @Test
    void findAllByItem_Owner_IdOrderByStartDesc() {

    }

    @Test
    void testFindAllByItem_Owner_IdOrderByStartDesc() {
    }

    @Test
    void findAllByItem_Owner_IdAndStatusOrderByStartDesc() {
    }

    @Test
    void findAllByBooker_IdOrderByStartDesc() {
    }

    @Test
    void testFindAllByBooker_IdOrderByStartDesc() {
    }

    @Test
    void findAllByBooker_IdAndStatusOrderByStartDesc() {
    }

    @Test
    void findAllByItem_Owner_IdAndStartAfterOrderByStartDesc() {
    }

    @Test
    void findAllByItem_Owner_IdAndEndBeforeOrderByStartDesc() {
    }

    @Test
    void findAllByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc() {
    }

    @Test
    void findAllByBooker_IdAndStartAfterOrderByStartDesc() {
    }

    @Test
    void findAllByBooker_IdAndEndBeforeOrderByStartDesc() {
    }

    @Test
    void findAllByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc() {
    }

    @Test
    void findFirstByStartAfterAndItem_IdAndStatusInOrderByStartAsc() {
    }

    @Test
    void findFirstByStartBeforeAndItem_IdAndStatusInOrderByEndDesc() {
    }
}
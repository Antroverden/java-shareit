package ru.practicum.shareit.booking.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.service.ItemService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl {

    BookingRepository bookingRepository;
    ItemService itemService;

    public Booking addBooking(Booking booking) {
        if (booking.getEnd().isBefore(booking.getStart()) || booking.getStart().isEqual(booking.getEnd())
                || !itemService.getItemById(booking.getItem().getId()).getAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        booking.setStatus(Booking.Status.WAITING);
        return bookingRepository.save(booking);
    }

//    public Booking updateBooking(Booking booking) {
//        Integer ownerFromDBId = bookingRepository.findById(booking.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
//                "Айтема с айди " + booking.getId() + "не существует")).getId();
//        Integer ownerId = booking.getOwner().getId();
//        Booking bookingFromDB = getBookingById(booking.getId());
//        if (booking.getAvailable() != null) {
//            bookingFromDB.setAvailable(booking.getAvailable());
//        } else if (!ownerFromDBId.equals(ownerId)) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT,
//                    "Юзер с айди " + ownerId + "не является владельцем данной вещи");
//        }
//        if (booking.getName() != null) {
//            bookingFromDB.setName(booking.getName());
//        }
//        if (booking.getDescription() != null) {
//            bookingFromDB.setDescription(booking.getDescription());
//        }
//        if (booking.getAvailable() != null) {
//            bookingFromDB.setAvailable(booking.getAvailable());
//        }
//        return bookingRepository.save(bookingFromDB);
//    }
//
//
//    public Booking getBookingById(int id) {
//        return bookingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
//                "Айтема с айди " + id + "не существует"));
//    }
//
//
//    public List<Booking> getBookings(int ownerId) {
//        return bookingRepository.findAll().stream().filter(booking -> booking.getOwner().getId().equals(ownerId))
//                .collect(Collectors.toList());
//    }
//
//
//    public void deleteBooking(int id) {
//        bookingRepository.deleteById(id);
//    }
//
//
//    public List<Booking> searchBookings(String text) {
//        if (text.isBlank()) return List.of();
//        return bookingRepository.findAll().stream()
//                .filter(booking -> ((booking.getName().toLowerCase().contains(text.toLowerCase())
//                        || booking.getDescription().toLowerCase().contains(text.toLowerCase())) && booking.getAvailable()))
//                .collect(Collectors.toList());
//    }
}

//package ru.practicum.shareit.booking.service;
//
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//import ru.practicum.shareit.booking.Booking;
//import ru.practicum.shareit.booking.storage.BookingRepository;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class BookingServiceImpl {
//
//    BookingRepository bookingRepository;
//
//    public Booking addBooking(Booking booking) {
//        if (bookingRepository.findAll().stream().anyMatch(u -> u.getEmail().equals(booking.getEmail()))) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT,
//                    "Юзер с имейлом " + booking.getEmail() + "уже существует");
//        }
//        return bookingRepository.save(booking);
//    }
//
//
//    public Booking updateBooking(Booking booking) {
//        if (bookingRepository.findAll().stream()
//                .anyMatch(u -> (u.getEmail().equals(booking.getEmail()) && u.getId().equals(booking.getId())))) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT,
//                    "Юзер с имейлом " + booking.getEmail() + "уже существует");
//        }
//        return bookingRepository.save(booking);
//    }
//
//
//    public Booking getBookingById(int id) {
//        return bookingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
//                "Бронирования с айди " + id + "не существует"));
//    }
//
//    public List<Booking> getBookings() {
//        return bookingRepository.findAll();
//    }
//
//    public void deleteBooking(int id) {
//        bookingRepository.deleteById(id);
//    }
//}

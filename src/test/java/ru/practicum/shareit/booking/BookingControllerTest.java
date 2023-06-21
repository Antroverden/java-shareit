package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithIdsFullDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private BookingMapper bookingMapper;

    @Test
    void testAddBooking() throws Exception {
        BookingWithIdsFullDto bookingWithIdsFullDto = createSampleBookingWithIdsFullDto();
        BookingDto bookingDto = createSampleBookingDto();

        when(bookingMapper.toBooking(any(BookingWithIdsFullDto.class))).thenReturn(createSampleBooking());
        when(bookingService.add(any(Booking.class))).thenReturn(createSampleBooking());
        when(bookingMapper.toDto(any(Booking.class))).thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":2,\"start\":\"2023-06-22T11:22:50\",\"end\":\"2023-06-23T11:22:50\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void testApproveOrRejectBooking() throws Exception {
        BookingWithIdsFullDto bookingWithIdsFullDto = createSampleBookingWithIdsFullDto();
        BookingDto bookingDto = createSampleBookingDto();

        when(bookingMapper.toBooking(any(BookingWithIdsFullDto.class))).thenReturn(createSampleBooking());
        when(bookingService.update(any(Booking.class))).thenReturn(createSampleBooking());
        when(bookingMapper.toDto(any(Booking.class))).thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", "1")
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetBookingInfo() throws Exception {
        BookingDto sampleBookingDto = createSampleBookingDto();
        Booking sampleBooking = createSampleBooking();

        when(bookingService.getById(anyInt(), anyInt())).thenReturn(sampleBooking);
        when(bookingMapper.toDto(sampleBooking)).thenReturn(sampleBookingDto);

        mockMvc.perform(get("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": 1}"));
    }

    @Test
    public void testGetBookings() throws Exception {
        List<BookingDto> sampleBookingList = Collections.singletonList(createSampleBookingDto());
        List<Booking> sampleBookingEntities = Collections.singletonList(createSampleBooking());

        when(bookingService.getAll(anyInt(), anyString(), anyBoolean(), anyInt(), anyInt())).thenReturn(sampleBookingEntities);
        when(bookingMapper.toDto(sampleBookingEntities)).thenReturn(sampleBookingList);

        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\": 1}]"));
    }

    @Test
    public void testGetBookingsWithInfoOfItems() throws Exception {
        List<BookingDto> sampleBookingList = Collections.singletonList(createSampleBookingDto());
        List<Booking> sampleBookingEntities = Collections.singletonList(createSampleBooking());

        when(bookingService.getAll(anyInt(), anyString(), anyBoolean(), anyInt(), anyInt())).thenReturn(sampleBookingEntities);
        when(bookingMapper.toDto(sampleBookingEntities)).thenReturn(sampleBookingList);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\": 1}]"));
    }


    private BookingWithIdsFullDto createSampleBookingWithIdsFullDto() {
        BookingWithIdsFullDto bookingWithIdsFullDto = new BookingWithIdsFullDto();
        bookingWithIdsFullDto.setId(1);
        bookingWithIdsFullDto.setItemId(1);
        return bookingWithIdsFullDto;
    }

    private BookingDto createSampleBookingDto() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1);
        User user = new User(1, "Name", "email@rambler.ru");
        Item item = new Item();
        item.setId(1);
        item.setOwner(user);
        item.setAvailable(true);
        bookingDto.setItem(item);
        return bookingDto;
    }

    private Booking createSampleBooking() {
        Booking booking = new Booking();
        booking.setId(1);
        User user = new User(1, "Name", "email@rambler.ru");
        Item item = new Item();
        item.setId(1);
        item.setOwner(user);
        item.setAvailable(true);
        booking.setItem(item);
        return booking;
    }
}
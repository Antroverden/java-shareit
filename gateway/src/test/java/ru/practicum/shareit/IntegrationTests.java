package ru.practicum.shareit;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")

public class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(3)
    public void userCreateFailNoEmail() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"user\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(4)
    public void userCreateFailInvalidEmail() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"user\",\"email\":\"user.com\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(20)
    public void itemCreateWithoutAvailable() throws Exception {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "3")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Отвертка\",\"description\":\"Аккумуляторнаяотвертка\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(21)
    public void itemCreateWithEmptyName() throws Exception {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "3")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"\",\"description\":\"Аккумуляторнаяотвертка\",\"available\":true}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(22)
    public void itemCreateWithEmptyDescription() throws Exception {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "3")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Отвертка\",\"available\":true}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(51)
    public void bookingCreateFailedByStartEqualNull() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":2,\"start\":null,\"end\":\"2023-07-22T11:22:50\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(52)
    public void bookingCreateFailedByEndEqualNull() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":2,\"start\":\"2023-07-20T11:22:50\",\"end\":null}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(53)
    public void bookingCreateFailedByStartInPast() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":2,\"start\":\"2023-06-20T11:22:50\",\"end\":\"2023-07-22T11:22:50\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(101)
    public void commentFromUserToItemWithoutBookingFailed() throws Exception {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"text\":\"Comment for item 1\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(102)
    public void commentFromUserToItemEmptyFailed() throws Exception {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"text\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(107)
    public void addCommentToItemFromUserFailedByFutureBooking() throws Exception {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "5")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"text\":\"Add comment from user 5\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(114)
    public void addItemRequestWithEmptyDescription() throws Exception {
        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"description\":null}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(118)
    public void requestsGetAllWithFromEmptySize() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(119)
    public void requestsGetAllWithFromSizeAnother() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "-1")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(120)
    public void requestsGetAllWithFromSize() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(132)
    public void bookingsGetAllWithSize() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(133)
    public void bookingsOwnerGetAllWithFromSize() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(134)
    public void bookingsGetAllWithFromSizeAnother() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "-1")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(135)
    public void bookingsOwnerFetAllWithFromSize() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "-1")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(136)
    public void bookingsGetAllWithFromSize() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(137)
    public void bookingsAnotherOwnerGetAllWithFromSize() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}

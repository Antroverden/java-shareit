package ru.practicum.shareit;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    public void userCreate() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"user\",\"email\":\"user@user.com\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    public void userCreateFailDuplicateEmail() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"user\",\"email\":\"user@user.com\"}"))
                .andExpect(status().isConflict());
    }

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
    @Order(5)
    public void userUpdate() throws Exception {
        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"update\",\"email\":\"update@user.com\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    public void userAnotherCreate() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"user\",\"email\":\"user@user.com\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(7)
    public void userUpdateName() throws Exception {
        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"updateName\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    public void userUpdateEmail() throws Exception {
        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"updateName@user.com\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    public void userUpdateWithSameEmail() throws Exception {
        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"updateName@user.com\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(10)
    public void usernameUpdateFailEmailExists() throws Exception {
        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"user@user.com\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(11)
    public void userGetUpdated() throws Exception {
        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(12)
    public void userGet() throws Exception {
        mockMvc.perform(get("/users/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(13)
    public void userGetUnknown() throws Exception {
        mockMvc.perform(get("/users/100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(14)
    public void userDelete() throws Exception {
        mockMvc.perform(delete("/users/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(15)
    public void userCreateAfterDelete() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"user\",\"email\":\"user@user.com\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(16)
    public void userGetAll() throws Exception {
        mockMvc.perform(get("/users/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(17)
    public void itemCreateByUser() throws Exception {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Дрель\",\"description\":\"Простаядрель\",\"available\":true}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(18)
    public void itemCreateWithoutXSharerUserId() throws Exception {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Дрель\",\"description\":\"Простаядрель\",\"available\":true}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(19)
    public void itemCreateWithNotFoundUser() throws Exception {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "10")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Дрель\",\"description\":\"Простаядрель\",\"available\":true}"))
                .andExpect(status().isNotFound());
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
    @Order(23)
    public void itemUpdate() throws Exception {
        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"id\":1,\"name\":\"Дрель+\",\"description\":\"Аккумуляторнаядрель\",\"available\":false}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(24)
    public void itemUpdateWithoutXSharerUserId() throws Exception {
        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", "")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"id\":1,\"name\":\"Дрель\",\"description\":\"Простаядрель\",\"available\":false}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(25)
    public void itemUpdateWithoutUser() throws Exception {
        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", "3")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Дрель\",\"description\":\"Простаядрель\",\"available\":false}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(26)
    public void itemUpdateAvailable() throws Exception {
        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"available\":true}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(27)
    public void itemUpdateDescription() throws Exception {
        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"description\":\"Аккумуляторнаядрель+аккумулятор\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(28)
    public void itemUpdateName() throws Exception {
        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Аккумуляторнаядрель\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(29)
    public void itemGetFromOwnerUser() throws Exception {
        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(30)
    public void itemGetFromUser() throws Exception {
        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(31)
    public void itemGetFromAnotherUser() throws Exception {
        mockMvc.perform(get("/items/100")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(32)
    public void itemCreateByOtherUser() throws Exception {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Отвертка\",\"description\":\"Аккумуляторнаяотвертка\",\"available\":true}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(33)
    public void itemCreateByAnotherUser() throws Exception {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"КлейМомент\",\"description\":\"ТюбиксуперклеямаркиМомент\",\"available\":true}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(34)
    public void itemGetAllForUser() throws Exception {
        mockMvc.perform(get("/items/")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(35)
    public void itemGetAllUser() throws Exception {
        mockMvc.perform(get("/items/")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(36)
    public void itemSearch() throws Exception {
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", "1")
                        .param("text", "аккУМУляторная")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(37)
    public void itemUpdateSetUnavailable() throws Exception {
        mockMvc.perform(patch("/items/2")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"available\":false}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(38)
    public void itemAnotherSearch() throws Exception {
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", "1")
                        .param("text", "дРелЬ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(39)
    public void itemSearchAvailable() throws Exception {
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", "1")
                        .param("text", "аккУМУляторная")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(40)
    public void itemUpdateSetAvailable() throws Exception {
        mockMvc.perform(patch("/items/2")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"available\":true}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(41)
    public void itemSearchAndAnother() throws Exception {
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", "1")
                        .param("text", "оТверТ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(42)
    public void itemSearchEmpty() throws Exception {
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", "1")
                        .param("text", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(43)
    public void itemUpdateSetUnavailableAnother() throws Exception {
        mockMvc.perform(patch("/items/2")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"available\":false}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(44)
    public void bookingCreateFromUserToItemUnavailable() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":2,\"start\":\"2023-07-22T11:22:50\",\"end\":\"2023-07-23T11:22:50\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(45)
    public void itemUpdateSetAvailableAnother() throws Exception {
        mockMvc.perform(patch("/items/2")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"available\":true}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(46)
    public void bookingCreateFailedByWrongUserId() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "100")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":2,\"start\":\"2023-07-25T11:22:50\",\"end\":\"2023-07-26T11:22:50\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(47)
    public void bookingCreateFailedByNotFoundItemId() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":200,\"start\":\"2023-07-22T11:22:50\",\"end\":\"2023-07-23T11:22:50\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(48)
    public void bookingCreateFailedByEndInPast() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":2,\"start\":\"2023-07-22T11:22:50\",\"end\":\"2023-07-20T11:22:50\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(49)
    public void bookingCreateFailedByEndBeforeStart() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":2,\"start\":\"2023-07-23T11:22:50\",\"end\":\"2023-07-22T11:22:50\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(50)
    public void bookingCreateFailedByStartEqualEnd() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":2,\"start\":\"2023-07-23T11:22:50\",\"end\":\"2023-07-23T11:22:50\"}"))
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
    @Order(54)
    public void bookingCreateFromAnotherUserToItemCurrent() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":2,\"start\":\"2023-07-24T11:22:50\",\"end\":\"2023-07-25T11:22:50\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(55)
    public void bookingSetApproveByOwnerCurrent() throws Exception {
        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", "4")
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(56)
    public void bookingCreateFromUserToItem() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":2,\"start\":\"2023-07-22T11:22:50\",\"end\":\"2023-07-23T11:22:50\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(57)
    public void bookingGetByUserBooker() throws Exception {
        mockMvc.perform(get("/bookings/2")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(58)
    public void bookingGetByUserOwner() throws Exception {
        mockMvc.perform(get("/bookings/2")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(59)
    public void bookingGetAllForWrongUser() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(60)
    public void bookingGetAllForWrongOwnerUser() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(61)
    public void bookingGetAllForUser() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(62)
    public void bookingGetAllForUserByAllstate() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "ALL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(63)
    public void bookingGetAllForUserByFutureState() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "FUTURE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(64)
    public void bookingGetAllForUserByWrongState() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "UNSUPPORTED_STATUS")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(65)
    public void bookingGetAllForOwner() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(66)
    public void bookingGetAllForOwnerByAllState() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "4")
                        .param("state", "ALL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(67)
    public void bookingGetAllForOwnerByFutureState() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "4")
                        .param("state", "FUTURE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(68)
    public void bookingGetAllForOwnerByWrongState() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "4")
                        .param("state", "UNSUPPORTED_STATUS")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(69)
    public void bookingGetFromUser() throws Exception {
        mockMvc.perform(get("/bookings/1000")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(70)
    public void userYetAnotherCreate() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"other\",\"email\":\"other@other.com\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(71)
    public void bookingGetFromUserAnother() throws Exception {
        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(72)
    public void bookingChangeStatusFromUser() throws Exception {
        mockMvc.perform(patch("/bookings/2")
                        .header("X-Sharer-User-Id", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(73)
    public void bookingChangeStatusByUserBooker() throws Exception {
        mockMvc.perform(patch("/bookings/2")
                        .header("X-Sharer-User-Id", "1")
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(74)
    public void bookingSetApproveByUserOwner() throws Exception {
        mockMvc.perform(patch("/bookings/2")
                        .header("X-Sharer-User-Id", "4")
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(75)
    public void bookingChangeStatusByUserOwnerAfterApprove() throws Exception {
        mockMvc.perform(patch("/bookings/2")
                        .header("X-Sharer-User-Id", "4")
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(76)
    public void userCreateYetAnother() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"practicum\",\"email\":\"practicum@yandex.ru\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(77)
    public void bookingCreateFromUserToItemFailed() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":1,\"start\":\"2023-07-23T11:22:50\",\"end\":\"2023-07-24T11:22:50\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(80)
    public void bookingCreateFromUserToAnotherItem() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "5")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":2,\"start\":\"2023-07-22T11:22:50\",\"end\":\"2023-07-23T11:22:50\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(82)
    public void itemGetByUserOwnerWithBookings() throws Exception {
        mockMvc.perform(get("/items/2")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(83)
    public void itemGetByUserWithoutBookings() throws Exception {
        mockMvc.perform(get("/items/2")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(84)
    public void itemGetByAnotherUserWithoutBookings() throws Exception {
        mockMvc.perform(get("/items/2")
                        .header("X-Sharer-User-Id", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(85)
    public void itemGetAllUserWithBookings() throws Exception {
        mockMvc.perform(get("/items/")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    @Order(87)
    public void bookingGetAllForUserByWaitingState() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "WAITING")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(88)
    public void bookingGetAllForUserOwnerByWaitingState() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "4")
                        .param("state", "WAITING")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(89)
    public void bookingCreateFromUserToItemCurrent() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":2,\"start\":\"2023-07-24T11:22:50\",\"end\":\"2023-07-25T11:22:50\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(91)
    public void bookingGetAllForUserByRejectedState() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "REJECTED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(92)
    public void bookingGetAllForUserByRejectedStateAnother() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "4")
                        .param("state", "REJECTED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(94)
    public void itemAnotherCreateByUser() throws Exception {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "6")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Кухонныйстол\",\"description\":\"Столдляпразднования\",\"available\":true}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(95)
    public void itemGetFromUserOwnerWithoutComments() throws Exception {
        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(99)
    public void itemGetFromUserWithoutComments() throws Exception {
        mockMvc.perform(get("/items/2")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(100)
    public void itemGetFromAnotherUserOwnerWithoutComments() throws Exception {
        mockMvc.perform(get("/items/2")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
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
    @Order(104)
    public void itemGetByUserWithComments() throws Exception {
        mockMvc.perform(get("/items/2")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(105)
    public void itemGetByUserOwnerWithComments() throws Exception {
        mockMvc.perform(get("/items/2")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
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
    @Order(108)
    public void bookingGetAllForUserByCurrentState() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "CURRENT")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(109)
    public void bookingGetAllForUserOwnerByCurrentState() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "4")
                        .param("state", "CURRENT")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(110)
    public void bookingGetAllForUserByPastState() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "PAST")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(111)
    public void bookingGetAllForUserOwnerByPastState() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "4")
                        .param("state", "PAST")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(112)
    public void itemGetByUserOwnerWithoutComments() throws Exception {
        mockMvc.perform(get("/items/4")
                        .header("X-Sharer-User-Id", "6")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(113)
    public void addItemRequestWithWrongUser() throws Exception {
        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "99")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"description\":\"Хотелбывоспользоватьсящёткойдляобуви\"}"))
                .andExpect(status().isNotFound());
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
    @Order(115)
    public void requestsGetOwnByWrongUserId() throws Exception {
        mockMvc.perform(get("/requests/")
                        .header("X-Sharer-User-Id", "99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(116)
    public void requestsGetOwnByUserIdWithoutRequests() throws Exception {
        mockMvc.perform(get("/requests/")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(117)
    public void requestsGetAllWithoutPaginationParams() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
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
    @Order(121)
    public void requestsGetAllWithFromSizeWhenEmpty() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(122)
    public void addItemRequest() throws Exception {
        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"description\":\"Хотелбывоспользоватьсящёткойдляобуви\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(123)
    public void requestsGetOwnByUserIdWithRequestsEmptyItems() throws Exception {
        mockMvc.perform(get("/requests/")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(124)
    public void addItemToRequestFromUser() throws Exception {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Щёткадляобуви\",\"description\":\"Стандартнаящёткадляобуви\",\"available\":true,\"requestId\":1}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(125)
    public void requestsGetOwnByUserIdWithRequests() throws Exception {
        mockMvc.perform(get("/requests/")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(126)
    public void requestsGetAllWithFromSizeForRequestOwner() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(127)
    public void requestsGetAllWithFromSizeForOtherUser() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "4")
                        .param("from", "0")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(128)
    public void requestsGetByIdForWrongUser() throws Exception {
        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", "99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(129)
    public void requestsGetByWrongId() throws Exception {
        mockMvc.perform(get("/requests/99")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(130)
    public void requestsGetByIdForOtherUser() throws Exception {
        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(131)
    public void requestsGetById() throws Exception {
        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
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

    @Test
    @Order(138)
    public void bookingsGetAnotherAllWithFromSize() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "4")
                        .param("size", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(139)
    public void bookingsOwnerGetAllWithFromSizeWhenAll() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "1")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}

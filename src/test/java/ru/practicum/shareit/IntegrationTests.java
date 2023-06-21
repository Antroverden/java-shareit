package ru.practicum.shareit;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    @Test
    @Order(1)
    public void User1create1() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"user\",\"email\":\"user@user.com\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    public void User2createfailduplicateemail2() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"user\",\"email\":\"user@user.com\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(3)
    public void Usercreatefailnoemail3() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"user\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(4)
    public void Usercreatefailinvalidemail4() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"user\",\"email\":\"user.com\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    public void User1update5() throws Exception {
        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"update\",\"email\":\"update@user.com\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    public void User3create6() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"user\",\"email\":\"user@user.com\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(7)
    public void User1updatename7() throws Exception {
        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"updateName\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    public void User1updateemail8() throws Exception {
        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"updateName@user.com\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    public void User1updatewithsameemail9() throws Exception {
        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"updateName@user.com\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(10)
    public void User1nameupdatefailemailexists10() throws Exception {
        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"user@user.com\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(11)
    public void User1getupdated11() throws Exception {
        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(12)
    public void User3get12() throws Exception {
        mockMvc.perform(get("/users/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(13)
    public void User100getunkonwn13() throws Exception {
        mockMvc.perform(get("/users/100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(14)
    public void User3delete14() throws Exception {
        mockMvc.perform(delete("/users/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(15)
    public void User4createafterdelete15() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"user\",\"email\":\"user@user.com\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(16)
    public void Usergetall16() throws Exception {
        mockMvc.perform(get("/users/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(17)
    public void Item1createbyuser117() throws Exception {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Дрель\",\"description\":\"Простаядрель\",\"available\":true}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(18)
    public void ItemcreatewithoutXSharerUserId18() throws Exception {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Дрель\",\"description\":\"Простаядрель\",\"available\":true}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(19)
    public void Itemcreatewithnotfounduser19() throws Exception {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "10")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Дрель\",\"description\":\"Простаядрель\",\"available\":true}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(20)
    public void Itemcreatewithoutavailable20() throws Exception {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "3")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Отвертка\",\"description\":\"Аккумуляторнаяотвертка\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(21)
    public void Itemcreatewithemptyname21() throws Exception {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "3")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"\",\"description\":\"Аккумуляторнаяотвертка\",\"available\":true}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(22)
    public void Itemcreatewithemptydescription22() throws Exception {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "3")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Отвертка\",\"available\":true}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(23)
    public void item1update23() throws Exception {
        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"id\":1,\"name\":\"Дрель+\",\"description\":\"Аккумуляторнаядрель\",\"available\":false}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(24)
    public void item1updatewithoutXSharerUserId24() throws Exception {
        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", "")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"id\":1,\"name\":\"Дрель\",\"description\":\"Простаядрель\",\"available\":false}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(25)
    public void item1updatewithotheruser325() throws Exception {
        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", "3")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Дрель\",\"description\":\"Простаядрель\",\"available\":false}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(26)
    public void item1updateavailable26() throws Exception {
        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"available\":true}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(27)
    public void item1updatedescription27() throws Exception {
        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"description\":\"Аккумуляторнаядрель+аккумулятор\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(28)
    public void item1updatename28() throws Exception {
        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Аккумуляторнаядрель\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(29)
    public void Item1getfromowneruser129() throws Exception {
        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(30)
    public void Item1getfromuser430() throws Exception {
        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(31)
    public void Item100getfromuser431() throws Exception {
        mockMvc.perform(get("/items/100")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(32)
    public void Item2createbyuser432() throws Exception {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Отвертка\",\"description\":\"Аккумуляторнаяотвертка\",\"available\":true}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(33)
    public void Item3createbyuser433() throws Exception {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"КлейМомент\",\"description\":\"ТюбиксуперклеямаркиМомент\",\"available\":true}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(34)
    public void Itemgetalluser134() throws Exception {
        mockMvc.perform(get("/items/")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(35)
    public void Itemgetalluser435() throws Exception {
        mockMvc.perform(get("/items/")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(36)
    public void Itemsearchаккумуляторная36() throws Exception {
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", "1")
                        .param("text", "аккУМУляторная")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(37)
    public void item2updatesetunavailable37() throws Exception {
        mockMvc.perform(patch("/items/2")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"available\":false}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(38)
    public void Itemsearchдрель38() throws Exception {
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", "1")
                        .param("text", "дРелЬ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(39)
    public void Itemsearchаккумуляторнаяavailable39() throws Exception {
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", "1")
                        .param("text", "аккУМУляторная")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(40)
    public void item2updatesetavailable40() throws Exception {
        mockMvc.perform(patch("/items/2")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"available\":true}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(41)
    public void Itemsearchотвертка41() throws Exception {
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", "1")
                        .param("text", "оТверТ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(42)
    public void Itemsearchempty42() throws Exception {
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", "1")
                        .param("text", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(43)
    public void item2updatesetunavailable43() throws Exception {
        mockMvc.perform(patch("/items/2")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"available\":false}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(44)
    public void Bookingcreatefromuser1toitem2unavailable44() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":2,\"start\":\"2023-06-22T11:22:50\",\"end\":\"2023-06-23T11:22:50\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(45)
    public void item2updatesetavailable45() throws Exception {
        mockMvc.perform(patch("/items/2")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"available\":true}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(46)
    public void BookingcreatefailedbywronguserId46() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "100")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":2,\"start\":\"2023-06-22T11:22:50\",\"end\":\"2023-06-23T11:22:50\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(47)
    public void BookingcreatefailedbynotfounditemId47() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":200,\"start\":\"2023-06-22T11:22:50\",\"end\":\"2023-06-23T11:22:50\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(48)
    public void Bookingcreatefailedbyendinpast48() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":2,\"start\":\"2023-06-22T11:22:50\",\"end\":\"2023-06-20T11:22:50\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(49)
    public void Bookingcreatefailedbyendbeforestart49() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":2,\"start\":\"2023-06-23T11:22:50\",\"end\":\"2023-06-22T11:22:50\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(50)
    public void Bookingcreatefailedbystartequalend50() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":2,\"start\":\"2023-06-23T11:22:50\",\"end\":\"2023-06-23T11:22:50\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(51)
    public void Bookingcreatefailedbystartequalnull51() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":2,\"start\":null,\"end\":\"2023-06-22T11:22:50\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(52)
    public void Bookingcreatefailedbyendequalnull52() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":2,\"start\":\"2023-06-20T11:22:50\",\"end\":null}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(53)
    public void Bookingcreatefailedbystartinpast53() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":2,\"start\":\"2023-06-20T11:22:50\",\"end\":\"2023-06-22T11:22:50\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(54)
    public void Booking1createfromuser1toitem2current54() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":2,\"start\":\"2023-06-24T11:22:50\",\"end\":\"2023-06-25T11:22:50\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(55)
    public void Bookingsetapprovebyownercurrent55() throws Exception {
        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", "4")
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(56)
    public void Booking2createfromuser1toitem256() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":2,\"start\":\"2023-06-22T11:22:50\",\"end\":\"2023-06-23T11:22:50\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(57)
    public void Booking2getbyuser1booker57() throws Exception {
        mockMvc.perform(get("/bookings/2")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(58)
    public void Booking2getbyuser4owner58() throws Exception {
        mockMvc.perform(get("/bookings/2")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(59)
    public void Bookinggetallforwronguser10059() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(60)
    public void Bookinggetallforwrongowneruser10060() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(61)
    public void Bookinggetallforuser161() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(62)
    public void Bookinggetallforuser1byALLstate62() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "ALL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(63)
    public void Bookinggetallforuser1byFUTUREstate63() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "FUTURE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(64)
    public void Bookinggetallforuser1bywrongstate64() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "UNSUPPORTED_STATUS")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(65)
    public void Bookinggetallforowner65() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(66)
    public void BookinggetallforownerbyALLstate66() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "4")
                        .param("state", "ALL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(67)
    public void BookinggetallforownerbyFUTUREstate67() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "4")
                        .param("state", "FUTURE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(68)
    public void Bookinggetallforownerbywrongstate68() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "4")
                        .param("state", "UNSUPPORTED_STATUS")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(69)
    public void Booking1000getfromuser469() throws Exception {
        mockMvc.perform(get("/bookings/1000")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(70)
    public void User5create70() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"other\",\"email\":\"other@other.com\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(71)
    public void Booking1getfromuser571() throws Exception {
        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(72)
    public void Booking2changestatusfromuser5fail72() throws Exception {
        mockMvc.perform(patch("/bookings/2")
                        .header("X-Sharer-User-Id", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(73)
    public void Booking2changestatusbyuser1booker73() throws Exception {
        mockMvc.perform(patch("/bookings/2")
                        .header("X-Sharer-User-Id", "1")
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(74)
    public void Booking2setapprovebyuser4owner74() throws Exception {
        mockMvc.perform(patch("/bookings/2")
                        .header("X-Sharer-User-Id", "4")
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(75)
    public void Booking2changestatusbyuser4ownerafterapprove75() throws Exception {
        mockMvc.perform(patch("/bookings/2")
                        .header("X-Sharer-User-Id", "4")
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(76)
    public void User6create76() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"practicum\",\"email\":\"practicum@yandex.ru\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(77)
    public void Bookingcreatefromuser1toitem1failed77() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":1,\"start\":\"2023-06-23T11:22:50\",\"end\":\"2023-06-24T11:22:50\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(80)
    public void Booking4createfromuser5toitem280() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "5")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":2,\"start\":\"2023-06-22T11:22:50\",\"end\":\"2023-06-23T11:22:50\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(82)
    public void Item2getbyuser4ownerwithbookings82() throws Exception {
        mockMvc.perform(get("/items/2")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(83)
    public void Item2getbyuser1withoutbookings83() throws Exception {
        mockMvc.perform(get("/items/2")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(84)
    public void Item2getbyuser5withoutbookings84() throws Exception {
        mockMvc.perform(get("/items/2")
                        .header("X-Sharer-User-Id", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(85)
    public void Itemgetalluser4withbookings85() throws Exception {
        mockMvc.perform(get("/items/")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    @Order(87)
    public void Bookinggetallforuser1byWAITINGstate87() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "WAITING")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(88)
    public void Bookinggetallforuser4ownerbyWAITINGstate88() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "4")
                        .param("state", "WAITING")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(89)
    public void Booking6createfromuser1toitem2current89() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"itemId\":2,\"start\":\"2023-06-24T11:22:50\",\"end\":\"2023-06-25T11:22:50\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(91)
    public void Bookinggetallforuser1byREJECTEDstate91() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "REJECTED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(92)
    public void Bookinggetallforuser4ownerbyREJECTEDstate92() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "4")
                        .param("state", "REJECTED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(94)
    public void Item4createbyuser694() throws Exception {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "6")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Кухонныйстол\",\"description\":\"Столдляпразднования\",\"available\":true}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(95)
    public void Item1getfromuser1ownerwithoutcomments95() throws Exception {
        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(99)
    public void Item2getfromuser1withoutcomments99() throws Exception {
        mockMvc.perform(get("/items/2")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(100)
    public void Item2getfromuser4ownerwithoutcomments100() throws Exception {
        mockMvc.perform(get("/items/2")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(101)
    public void Commentfromuser4toitem1withoutbookingfailed101() throws Exception {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"text\":\"Commentforitem1\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(102)
    public void Commentfromuser1toitem2emptyfailed102() throws Exception {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"text\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(104)
    public void Item2getbyuser1withcomments104() throws Exception {
        mockMvc.perform(get("/items/2")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(105)
    public void Item2getbyuser4ownerwithcomments105() throws Exception {
        mockMvc.perform(get("/items/2")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(107)
    public void Addcommenttoitem1fromuser5failedbyfuturebooking107() throws Exception {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "5")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"text\":\"Addcommentfromuser5\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(108)
    public void Bookinggetallforuser1byCURRENTstate108() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "CURRENT")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(109)
    public void Bookinggetallforuser4ownerbyCURRENTstate109() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "4")
                        .param("state", "CURRENT")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(110)
    public void Bookinggetallforuser1byPASTstate110() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "PAST")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(111)
    public void Bookinggetallforuser4ownerbyPASTstate111() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "4")
                        .param("state", "PAST")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(112)
    public void Item4getbyuser6ownerwithoutcomments112() throws Exception {
        mockMvc.perform(get("/items/4")
                        .header("X-Sharer-User-Id", "6")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(113)
    public void Additemrequestwithwronguser113() throws Exception {
        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "99")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"description\":\"Хотелбывоспользоватьсящёткойдляобуви\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(114)
    public void Additemrequestwithemptydescription114() throws Exception {
        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"description\":null}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(115)
    public void Requestsgetownbywronguserid115() throws Exception {
        mockMvc.perform(get("/requests/")
                        .header("X-Sharer-User-Id", "99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(116)
    public void Requestsgetownbyuseridwithoutrequests116() throws Exception {
        mockMvc.perform(get("/requests/")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(117)
    public void Requestsgetallwithoutpaginationparams117() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(118)
    public void Requestsgetallwithfrom0size0118() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(119)
    public void Requestsgetallwithfrom1size20119() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "-1")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(120)
    public void Requestsgetallwithfrom0size1120() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(121)
    public void Requestsgetallwithfrom0size20whenempty121() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(122)
    public void Additemrequest1122() throws Exception {
        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"description\":\"Хотелбывоспользоватьсящёткойдляобуви\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(123)
    public void Requestsgetownbyuseridwithrequestsemptyitems123() throws Exception {
        mockMvc.perform(get("/requests/")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(124)
    public void Additem5torequest1fromuser4124() throws Exception {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Щёткадляобуви\",\"description\":\"Стандартнаящёткадляобуви\",\"available\":true,\"requestId\":1}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(125)
    public void Requestsgetownbyuseridwithrequests125() throws Exception {
        mockMvc.perform(get("/requests/")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(126)
    public void Requestsgetallwithfrom0size20forrequestowner126() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(127)
    public void Requestsgetallwithfrom0size20forotheruser127() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "4")
                        .param("from", "0")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(128)
    public void Requestsgetbyidforwronguser128() throws Exception {
        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", "99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(129)
    public void Requestsgetbywrongid129() throws Exception {
        mockMvc.perform(get("/requests/99")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(130)
    public void Requestsgetbyidforotheruser130() throws Exception {
        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(131)
    public void Requestsgetbyid131() throws Exception {
        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(132)
    public void Bookingsgetallwithfrom0size0132() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(133)
    public void Bookingsownergetallwithfrom0size0133() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(134)
    public void Bookingsgetallwithfrom1size20134() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "-1")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(135)
    public void Bookingsownergetallwithfrom1size20135() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "-1")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(136)
    public void Bookingsgetallwithfrom0size1136() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(137)
    public void Bookingsownergetallwithfrom0size1137() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(138)
    public void Bookingsgetallwithfrom4size2whenall5138() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "4")
                        .param("size", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(139)
    public void Bookingsownergetallwithfrom1size1whenall2139() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "1")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }





}

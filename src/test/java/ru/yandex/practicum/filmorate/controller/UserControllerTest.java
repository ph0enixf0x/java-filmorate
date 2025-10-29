package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void checkCreateNewUser() throws Exception {
        String userJson = "{\"login\":\"dolore\",\"name\":\"Nick Name\",\"email\":\"mail@mail.ru\"," +
                "\"birthday\":\"1946-08-20\"}";

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.email").value("mail@mail.ru"))
                .andExpect(jsonPath("$.login").value("dolore"))
                .andExpect(jsonPath("$.name").value("Nick Name"))
                .andExpect(jsonPath("$.birthday").value("1946-08-20"));
    }

    @Test
    void checkCreateUserWrongLogin() throws Exception {
        String userJson = "{\"login\":\"dolore dolores\",\"name\":\"Nick Name\",\"email\":\"mail@mail.ru\"," +
                "\"birthday\":\"1946-08-20\"}";

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isInternalServerError())
                .andExpect(result ->
                        assertEquals("Логин пользователя не может отсутствовать или содержать пробелы",
                                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void checkCreateUserWrongEmail() throws Exception {
        String userJson = "{\"login\":\"dolore\",\"name\":\"Nick Name\",\"email\":\"mailmail.ru\"" +
                ",\"birthday\":\"1946-08-20\"}";

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isInternalServerError())
                .andExpect(result ->
                        assertEquals("Электронная почта пользователя отсутствует или задана не корректно",
                                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void checkCreateUserWrongBirthday() throws Exception {
        String userJson = "{\"login\":\"dolore\",\"name\":\"Nick Name\",\"email\":\"mail@mail.ru\"," +
                "\"birthday\":\"3000-08-20\"}";

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isInternalServerError())
                .andExpect(result ->
                        assertEquals("Дата рождения пользователя не может быть в будущем",
                                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void checkUpdateUser() throws Exception {
        String updatedUserJson = "{\"id\":\"1\",\"login\":\"dolores\",\"name\":\"Name Nick\"," +
                "\"email\":\"mail@yandex.ru\",\"birthday\":\"1990-08-20\"}";

        this.mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("mail@yandex.ru"))
                .andExpect(jsonPath("$.login").value("dolores"))
                .andExpect(jsonPath("$.name").value("Name Nick"))
                .andExpect(jsonPath("$.birthday").value("1990-08-20"));
    }

    @Test
    void checkUpdateUnknownUser() throws Exception {
        String updatedUserJson = "{\"id\":\"0\",\"login\":\"dolores\",\"name\":\"Name Nick\"," +
                "\"email\":\"mail@yandex.ru\",\"birthday\":\"1990-08-20\"}";

        this.mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void checkCreateNewEmptyNameUser() throws Exception {
        String userJson = "{\"login\":\"dolore\",\"name\":\"\",\"email\":\"mail@mail.ru\",\"birthday\":\"1946-08-20\"}";

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("mail@mail.ru"))
                .andExpect(jsonPath("$.login").value("dolore"))
                .andExpect(jsonPath("$.name").value("dolore"))
                .andExpect(jsonPath("$.birthday").value("1946-08-20"));
    }

    @Test
    void checkCollectAllUsers() throws Exception {
        this.mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].email").value("mail@yandex.ru"))
                .andExpect(jsonPath("$[0].login").value("dolores"))
                .andExpect(jsonPath("$[0].name").value("Name Nick"))
                .andExpect(jsonPath("$[0].birthday").value("1990-08-20"));
    }
}

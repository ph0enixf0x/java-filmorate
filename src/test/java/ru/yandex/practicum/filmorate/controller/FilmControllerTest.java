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
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void checkCreateNewFilm() throws Exception {
        String filmJson = "{\"name\":\"nisi eiusmod\",\"description\":\"adipisicing\",\"releaseDate\":\"1967-03-25\"," +
                "\"duration\":100}";

        this.mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.description").value("adipisicing"))
                .andExpect(jsonPath("$.releaseDate").value("1967-03-25"))
                .andExpect(jsonPath("$.name").value("nisi eiusmod"))
                .andExpect(jsonPath("$.duration").value("100"));
    }

    @Test
    void checkCreateFilmWrongName() throws Exception {
        String filmJson = "{\"name\":\"\",\"description\":\"adipisicing\",\"releaseDate\":\"1967-03-25\"," +
                "\"duration\":100}";

        this.mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isInternalServerError())
                .andExpect(result ->
                        assertEquals("Название фильма не может быть пустым",
                                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void checkCreateFilmWrongDescription() throws Exception {
        String filmJson = "{\"name\":\"nisi eiusmod\"," +
                "\"description\":\"012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890\"," +
                "\"releaseDate\":\"1967-03-25\",\"duration\":100}";

        String film2Json = "{\"name\":\"nisi eiusmod\"," +
                "\"description\":\"01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789\"," +
                "\"releaseDate\":\"1967-03-25\",\"duration\":100}";

        this.mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isInternalServerError())
                .andExpect(result ->
                        assertEquals("Размер описания фильма (201) больше 200 символов",
                                Objects.requireNonNull(result.getResolvedException()).getMessage()));

        this.mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(film2Json))
                .andExpect(status().isOk());
    }

    @Test
    void checkCreateFilmWrongReleaseDate() throws Exception {
        String filmJson = "{\"name\":\"nisi eiusmod\",\"description\":\"adipisicing\",\"releaseDate\":\"1895-12-27\"," +
                "\"duration\":100}";
        String film2Json = "{\"name\":\"nisi eiusmod\",\"description\":\"adipisicing\",\"releaseDate\":\"1895-12-28\"," +
                "\"duration\":100}";

        this.mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isInternalServerError())
                .andExpect(result ->
                        assertEquals("Дата выхода фильма (1895-12-27) указана до дня рождения кино " +
                                        "(28 декабря 1895)",
                                Objects.requireNonNull(result.getResolvedException()).getMessage()));

        this.mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(film2Json))
                .andExpect(status().isOk());
    }

    @Test
    void checkCreateFilmWrongDuration() throws Exception {
        String filmJson = "{\"name\":\"nisi eiusmod\",\"description\":\"adipisicing\",\"releaseDate\":\"1967-03-25\"," +
                "\"duration\":0}";
        String film2Json = "{\"name\":\"nisi eiusmod\",\"description\":\"adipisicing\",\"releaseDate\":\"1967-03-25\"," +
                "\"duration\":1}";

        this.mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isInternalServerError())
                .andExpect(result ->
                        assertEquals("Длительность фильма должна быть положительной",
                                Objects.requireNonNull(result.getResolvedException()).getMessage()));

        this.mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(film2Json))
                .andExpect(status().isOk());
    }

    @Test
    void checkUpdateFilm() throws Exception {
        String filmJson = "{\"name\":\"nisi eiusmod\",\"description\":\"adipisicing\",\"releaseDate\":\"1967-03-25\"," +
                "\"duration\":100}";
        String updatedFilmJson = "{\"id\":\"1\",\"name\":\"oma soma\",\"description\":\"otherwise\"," +
                "\"releaseDate\":\"1980-03-25\",\"duration\":50}";

        this.mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isOk());

        this.mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedFilmJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("oma soma"))
                .andExpect(jsonPath("$.description").value("otherwise"))
                .andExpect(jsonPath("$.releaseDate").value("1980-03-25"))
                .andExpect(jsonPath("$.duration").value("50"));
    }

    @Test
    void checkUpdateUnknownFilm() throws Exception {
        String updatedFilmJson = "{\"id\":\"0\",\"name\":\"oma soma\",\"description\":\"otherwise\"," +
                "\"releaseDate\":\"1980-03-25\",\"duration\":50}";

        this.mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedFilmJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void checkCollectAllFilms() throws Exception {

        this.mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("oma soma"))
                .andExpect(jsonPath("$[0].description").value("otherwise"))
                .andExpect(jsonPath("$[0].releaseDate").value("1980-03-25"))
                .andExpect(jsonPath("$[0].duration").value("50"));
    }
}

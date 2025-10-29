package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class FilmorateApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void checkCreateNewUser() throws Exception {
		String userJson = """
                {
                \t"login": "dolore",
                \t"name": "Nick Name",
                \t"email": "mail@mail.ru",
                \t"birthday": "1946-08-20"
                }""";

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
		String userJson = """
                {
                \t"login": "dolore dolores",
                \t"name": "Nick Name",
                \t"email": "mail@mail.ru",
                \t"birthday": "1946-08-20"
                }""";

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
		String userJson = """
                {
                \t"login": "dolore",
                \t"name": "Nick Name",
                \t"email": "mailmail.ru",
                \t"birthday": "1946-08-20"
                }""";

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
		String userJson = """
                {
                \t"login": "dolore",
                \t"name": "Nick Name",
                \t"email": "mail@mail.ru",
                \t"birthday": "3000-08-20"
                }""";

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

		String updatedUserJson = """
                {
                \t"id": "1",
                \t"login": "dolores",
                \t"name": "Name Nick",
                \t"email": "mail@yandex.ru",
                \t"birthday": "1990-08-20"
                }""";

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
		String updatedUserJson = """
                {
                \t"id": "0",
                \t"login": "dolores",
                \t"name": "Name Nick",
                \t"email": "mail@yandex.ru",
                \t"birthday": "1990-08-20"
                }""";

		this.mockMvc.perform(put("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(updatedUserJson))
				.andExpect(status().isNotFound());
	}

	@Test
	void checkCreateNewEmptyNameUser() throws Exception {
		String userJson = """
                {
                \t"login": "dolore",
                \t"name": "",
                \t"email": "mail@mail.ru",
                \t"birthday": "1946-08-20"
                }""";

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

	@Test
	void checkCreateNewFilm() throws Exception {
		String filmJson = """
                {
                \t"name": "nisi eiusmod",
                \t"description": "adipisicing",
                \t"releaseDate": "1967-03-25",
                \t"duration": 100
                }""";

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
		String filmJson = """
                {
                \t"name": "",
                \t"description": "adipisicing",
                \t"releaseDate": "1967-03-25",
                \t"duration": 100
                }""";

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
		String filmJson = """
                {
                \t"name": "nisi eiusmod",
                \t"description": "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890",
                \t"releaseDate": "1967-03-25",
                \t"duration": 100
                }""";

		String film2Json = """
                {
                \t"name": "nisi eiusmod",
                \t"description": "01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789",
                \t"releaseDate": "1967-03-25",
                \t"duration": 100
                }""";

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
		String filmJson = """
                {
                \t"name": "nisi eiusmod",
                \t"description": "adipisicing",
                \t"releaseDate": "1895-12-27",
                \t"duration": 100
                }""";
		String film2Json = """
                {
                \t"name": "nisi eiusmod",
                \t"description": "adipisicing",
                \t"releaseDate": "1895-12-28",
                \t"duration": 100
                }""";

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
		String filmJson = """
                {
                \t"name": "nisi eiusmod",
                \t"description": "adipisicing",
                \t"releaseDate": "1967-03-25",
                \t"duration": 0
                }""";
		String film2Json = """
                {
                \t"name": "nisi eiusmod",
                \t"description": "adipisicing",
                \t"releaseDate": "1967-03-25",
                \t"duration": 1
                }""";

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
		String filmJson = """
                {
                \t"name": "nisi eiusmod",
                \t"description": "adipisicing",
                \t"releaseDate": "1967-03-25",
                \t"duration": 100
                }""";
		String updatedFilmJson = """
                {
                \t"id": "1",
                \t"name": "oma soma",
                \t"description": "otherwise",
                \t"releaseDate": "1980-03-25",
                \t"duration": 50
                }""";

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
		String updatedFilmJson = """
                {
                \t"id": "0",
                \t"name": "oma soma",
                \t"description": "otherwise",
                \t"releaseDate": "1980-03-25",
                \t"duration": 50
                }""";

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

package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        validateFilmData(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Создан новый фильм: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        validateFilmData(newFilm);
        int filmId = newFilm.getId();
        if (films.containsKey(filmId)) {
            films.put(filmId, newFilm);
            log.info("Обновлен фильм: {}", newFilm);
            return newFilm;
        }
        log.error("Фильм с идентификатором {} не найден!", filmId);
        throw new NotFoundException("Фильм с идентификатором " + filmId + " не найден!");
    }

    private void validateFilmData(Film film) {
        String filmName = film.getName();
        if (filmName == null || filmName.isBlank()) {
            log.error("Название фильма не может быть пустым");
            throw new ValidationException("Название фильма не может быть пустым");
        }
        int filmDescriptionLength = film.getDescription().length();
        if (filmDescriptionLength > 200) {
            log.error("Размер опичания фильма ({}) больше 200 символов", filmDescriptionLength);
            throw new ValidationException("Размер описания фильма (" + filmDescriptionLength + ") больше 200 символов");
        }
        LocalDate filmReleaseDate = film.getReleaseDate();
        if (filmReleaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата выхода фильма ({}) указана до дня рождения кино (28 декабря 1895)", filmReleaseDate);
            throw new ValidationException("Дата выхода фильма (" + filmReleaseDate + ") " +
                    "указана до дня рождения кино (28 декабря 1895)");
        }
        if (film.getDuration() < 1) {
            log.error("Длительность фильма должна быть положительной");
            throw new ValidationException("Длительность фильма должна быть положительной");
        }
    }

    private int getNextId() {
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}

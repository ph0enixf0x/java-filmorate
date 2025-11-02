package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    @Autowired
    private FilmStorage filmStorage;

    @GetMapping
    public Collection<Film> getAll() {
        return filmStorage.getFilms();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validateFilmReleaseDate(film);
        return filmStorage.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        validateFilmReleaseDate(film);
        return filmStorage.update(film);
    }

    private void validateFilmReleaseDate(Film film) {
        LocalDate filmReleaseDate = film.getReleaseDate();
        if (filmReleaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата выхода фильма ({}) указана до дня рождения кино (28 декабря 1895)", filmReleaseDate);
            throw new ValidationException("Дата выхода фильма (" + filmReleaseDate + ") " +
                    "указана до дня рождения кино (28 декабря 1895)");
        }
    }
}

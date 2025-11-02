package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    @Autowired
    private FilmStorage filmStorage;
    @Autowired
    private FilmService filmService;

    @GetMapping
    public Collection<Film> getAll() {
        return filmStorage.getFilms();
    }

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable int filmId) {
        return filmStorage.getFilmById(filmId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Valid @RequestBody Film film) {
        validateFilmReleaseDate(film);
        return filmStorage.create(film);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film update(@Valid @RequestBody Film film) {
        validateFilmReleaseDate(film);
        return filmStorage.update(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addLike(
            @PathVariable int filmId,
            @PathVariable int userId
    ) {
        filmService.addLike(userId, filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(
            @PathVariable int filmId,
            @PathVariable int userId
    ) {
        filmService.removeLike(userId, filmId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        if (count <= 0) throw new ValidationException("Размер выборки популярных фильмов должен быть положительным");
        return filmService.getMostLikedFilms(count);
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

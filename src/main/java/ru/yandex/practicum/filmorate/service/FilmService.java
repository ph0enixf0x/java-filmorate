package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> getAllFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(int filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public Film createFilm(Film film) {
        validateFilmReleaseDate(film);
        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        validateFilmReleaseDate(film);
        return filmStorage.update(film);
    }

    public void addLike(int userId, int filmId) {
        log.debug("К фильму с ID {} добавляется лайк от пользователя с ID {}", filmId, userId);
        filmStorage.getFilmById(filmId).getLikes().add(userStorage.getUserById(userId).getId());
    }

    public void removeLike(int userId, int filmId) {
        log.debug("У фильма с ID {} удаляется лайк от пользователя с ID {}", filmId, userId);
        filmStorage.getFilmById(filmId).getLikes().remove(userStorage.getUserById(userId).getId());
    }

    public List<Film> getMostLikedFilms(int count) {
        return filmStorage.getFilms().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .toList();
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

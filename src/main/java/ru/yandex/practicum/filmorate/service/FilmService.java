package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class FilmService {

    @Autowired
    private FilmStorage filmStorage;
    @Autowired
    private UserStorage userStorage;

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
}

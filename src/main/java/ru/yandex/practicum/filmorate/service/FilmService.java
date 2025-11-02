package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;

@Service
public class FilmService {

    @Autowired
    private FilmStorage filmStorage;
    @Autowired
    private UserStorage userStorage;

    public void addLike(int userId, int filmId) {
        filmStorage.getFilmById(filmId).getLikes().add(userStorage.getUserById(userId).getId());
    }

    public void removeLike(int userId, int filmId) {
        filmStorage.getFilmById(filmId).getLikes().remove(userStorage.getUserById(userId).getId());
    }

    public List<Film> getMostLikedFilms(int count) {
        return filmStorage.getFilms().stream()
                .sorted(Comparator.comparingInt(film -> film.getLikes().size()))
                .limit(count)
                .toList();
    }
}

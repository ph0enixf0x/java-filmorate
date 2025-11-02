package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }

    @Override
    public Film getFilmById(int filmId) {
        if (films.containsKey(filmId)) {
            log.info("Запрошен фильм с ID: {}", filmId);
            return films.get(filmId);
        }
        throw new NotFoundException("Фильм с идентификатором " + filmId + " не найден!");
    }

    @Override
    public Film create(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Обновлен фильм {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        int filmId = film.getId();
        if (films.containsKey(filmId)) {
            films.put(filmId, film);
            log.info("Обновлен фильм: {}", film);
            return film;
        } else {
            throw new NotFoundException("Фильм с идентификатором " + filmId + " не найден!");
        }
    }

    @Override
    public void delete(int filmId) {
        films.remove(filmId);
        log.info("Удален фильм с ID: {}", filmId);
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

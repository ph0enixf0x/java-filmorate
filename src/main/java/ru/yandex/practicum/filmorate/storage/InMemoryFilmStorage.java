package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
            //log.info("Запрошен фильм с ID: {}", filmId);
            return films.get(filmId);
        }
        //log.error("Фильм с идентификатором {} не найден!", filmId);
        throw new NotFoundException("Фильм с идентификатором " + filmId + " не найден!");
    }

    @Override
    public void create(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public void update(Film film) {
        int filmId = film.getId();
        if (films.containsKey(filmId)) {
            films.put(filmId, film);
            //log.info("Обновлен фильм: {}", newFilm);
        } else {
            //log.error("Фильм с идентификатором {} не найден!", filmId);
            throw new NotFoundException("Фильм с идентификатором " + filmId + " не найден!");
        }
    }

    @Override
    public void delete(int filmId) {
        films.remove(filmId);
    }
}

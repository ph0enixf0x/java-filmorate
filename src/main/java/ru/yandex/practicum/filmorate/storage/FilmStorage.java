package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    public Collection<Film> getFilms();

    public Film getFilmById(int filmId);

    public void create(Film film);

    public void update(Film film);

    public void delete(int filmId);
}

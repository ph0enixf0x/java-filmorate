package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    public Collection<Film> getFilms();

    public Film getFilmById(int filmId);

    public Film create(Film film);

    public Film update(Film film);

    public void delete(int filmId);
}

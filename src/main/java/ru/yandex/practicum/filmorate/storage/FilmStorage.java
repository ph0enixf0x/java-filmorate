package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {
    public void create(Film film);

    public void update(Film film);

    public void delete(int filmId);
}

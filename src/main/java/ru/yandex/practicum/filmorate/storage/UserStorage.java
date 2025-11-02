package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {
    public void create(User user);

    public void update(User user);

    public void delete(int userId);
}

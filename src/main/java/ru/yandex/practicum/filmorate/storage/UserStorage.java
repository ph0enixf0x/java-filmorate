package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    public Collection<User> getUsers();

    public User getUserById(int userId);

    public void create(User user);

    public void update(User user);

    public void delete(int userId);
}

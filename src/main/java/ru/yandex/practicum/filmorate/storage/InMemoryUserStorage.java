package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public void create(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void update(User user) {
        int userId = user.getId();
        if (users.containsKey(userId)) {
            users.put(userId, user);
            //log.info("Обновлен пользователь: {}", newUser);
        } else {
            //log.error("Пользователь с идентификатором {} не найден!", userId);
            throw new NotFoundException("Пользователь с идентификатором " + userId + " не найден!");
        }
    }

    @Override
    public void delete(int userId) {
        users.remove(userId);
    }
}

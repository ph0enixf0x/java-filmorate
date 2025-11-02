package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User getUserById(int userId) {
        if (users.containsKey(userId)) {
            //log.info("Запрошен пользователь с ID: {}", userId);
            return users.get(userId);
        }
        //log.error("Пользователь с идентификатором {} не найден!", userId);
        throw new NotFoundException("Пользователь с идентификатором " + userId + " не найден!");
    }

    @Override
    public User create(User user) {
        user.setId(getNextId());
        String userName = user.getName();
        if (userName == null || userName.isBlank()) user.setName(user.getLogin());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        int userId = user.getId();
        if (users.containsKey(userId)) {
            String userName = user.getName();
            if (userName == null || userName.isBlank()) user.setName(user.getLogin());
            users.put(userId, user);
            //log.info("Обновлен пользователь: {}", newUser);
            return user;
        } else {
            //log.error("Пользователь с идентификатором {} не найден!", userId);
            throw new NotFoundException("Пользователь с идентификатором " + userId + " не найден!");
        }
    }

    @Override
    public void delete(int userId) {
        users.remove(userId);
    }

    private int getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}

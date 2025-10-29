package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        validateUserData(user);
        String userName = user.getName();
        if (userName == null || userName.isBlank()) user.setName(user.getLogin());
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Создан новый пользователь: {}", user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        validateUserData(newUser);
        int userId = newUser.getId();
        if (users.containsKey(userId)) {
            User oldUser = users.get(userId);
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setBirthday(newUser.getBirthday());
            String userName = newUser.getName();
            if (userName != null) oldUser.setName(userName);
            log.info("Обновлен пользователь: {}", oldUser);
            return oldUser;
        }
        log.error("Пользователь с идентификатором {} не найден!", userId);
        throw new NotFoundException("Пользователь с идентификатором " + userId + " не найден!");
    }

    private void validateUserData(User user) {
        String userEmail = user.getEmail();
        if (userEmail == null || userEmail.isBlank() || !userEmail.contains("@")) {
            log.error("Электронная почта пользователя отсутствует или задана не корректно");
            throw new ValidationException("Электронная почта пользователя отсутствует или задана не корректно");
        }
        String userLogin = user.getLogin();
        if (userLogin == null || userLogin.isBlank() || userLogin.contains(" ")) {
            log.error("Логин пользователя не может отсутствовать или содержать пробелы");
            throw new ValidationException("Логин пользователя не может отсутствовать или содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения пользователя не может быть в будущем");
            throw new ValidationException("Дата рождения пользователя не может быть в будущем");
        }
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

package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
            return oldUser;
        }
        throw new NotFoundException("Пользователь с идентификатором " + userId + " не найден!");
    }

    private void validateUserData(User user) {
        String userEmail = user.getEmail();
        if (userEmail == null || userEmail.isBlank() || !userEmail.contains("@")) {
            throw new ValidationException("Электронная почта пользователя отсутствует или задана не корректно");
        }
        String userLogin = user.getLogin();
        if (userLogin == null || userLogin.isBlank() || userLogin.contains(" ")) {
            throw new ValidationException("Логин пользователя не может отсутствовать или содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
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

package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserStorage userStorage;

    @GetMapping
    public Collection<User> getAll() {
        return userStorage.getUsers();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        validateUserLogin(user);
        return userStorage.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        validateUserLogin(user);
        return userStorage.update(user);
    }

    private void validateUserLogin(User user) {
        if (user.getLogin().contains(" ")) {
            log.error("Логин пользователя не может отсутствовать или содержать пробелы");
            throw new ValidationException("Логин пользователя не может отсутствовать или содержать пробелы");
        }
    }
}

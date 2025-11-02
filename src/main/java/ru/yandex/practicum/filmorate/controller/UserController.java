package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserStorage userStorage;
    @Autowired
    private UserService userService;

    @GetMapping
    public Collection<User> getAll() {
        return userStorage.getUsers();
    }

    @GetMapping("/{userId}")
    public User get(@PathVariable int userId) {
        return userStorage.getUserById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        validateUserLogin(user);
        return userStorage.create(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User update(@Valid @RequestBody User user) {
        validateUserLogin(user);
        return userStorage.update(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addFriend(
            @PathVariable int userId,
            @PathVariable int friendId
    ) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend(
            @PathVariable int userId,
            @PathVariable int friendId
    ) {
        userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@PathVariable int userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getCommonFriends(
            @PathVariable int userId,
            @PathVariable int otherId
    ) {
        return userService.getCommonFriends(userId, otherId);
    }

    private void validateUserLogin(User user) {
        if (user.getLogin().contains(" ")) {
            log.error("Логин пользователя не может отсутствовать или содержать пробелы");
            throw new ValidationException("Логин пользователя не может отсутствовать или содержать пробелы");
        }
    }
}

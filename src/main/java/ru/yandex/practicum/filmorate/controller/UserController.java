package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<User> getAll() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User get(@PathVariable int userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
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
}

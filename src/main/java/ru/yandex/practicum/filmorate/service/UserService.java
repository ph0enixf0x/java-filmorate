package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public Collection<User> getAllUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(int userId) {
        return userStorage.getUserById(userId);
    }

    public User createUser(User user) {
        validateUserLogin(user);
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        validateUserLogin(user);
        String userName = user.getName();
        if (userName == null || userName.isBlank()) user.setName(user.getLogin());
        return userStorage.update(user);
    }

    public void addFriend(int userId, int friendId) {
        User originalUser = userStorage.getUserById(userId);
        User friendUser = userStorage.getUserById(friendId);
        log.debug("Пользователю {} добавляется новый друг {}", originalUser, friendUser);
        originalUser.getFriends().add(friendId);
        friendUser.getFriends().add(userId);
    }

    public void removeFriend(int userId, int friendId) {
        User originalUser = userStorage.getUserById(userId);
        User friendUser = userStorage.getUserById(friendId);
        log.debug("У пользователя {} убирается друг {}", originalUser, friendUser);
        originalUser.getFriends().remove(friendId);
        friendUser.getFriends().remove(userId);
    }

    public List<User> getFriends(int userId) {
        return userStorage.getUserById(userId).getFriends().stream()
                .map(userStorage::getUserById)
                .toList();
    }

    public List<User> getCommonFriends(int userId, int otherUserId) {
        return userStorage.getUserById(userId).getFriends().stream()
                .filter(s -> userStorage.getUserById(otherUserId).getFriends().contains(s))
                .map(userStorage::getUserById)
                .toList();
    }

    private void validateUserLogin(User user) {
        if (user.getLogin().contains(" ")) {
            log.error("Логин пользователя не может отсутствовать или содержать пробелы");
            throw new ValidationException("Логин пользователя не может отсутствовать или содержать пробелы");
        }
    }
}

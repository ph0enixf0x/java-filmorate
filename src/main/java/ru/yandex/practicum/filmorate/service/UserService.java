package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public void addFriend(int userId, int friendId) {
        User originalUser = userStorage.getUserById(userId);
        User friendUser = userStorage.getUserById(friendId);
        log.debug("Польователю {} добавляется новый друг {}", originalUser, friendUser);
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
                .map(friendId -> userStorage.getUserById(friendId))
                .toList();
    }

    public List<User> getCommonFriends(int userId, int otherUserId) {
        return userStorage.getUserById(userId).getFriends().stream()
                .filter(s -> userStorage.getUserById(otherUserId).getFriends().contains(s))
                .map(friendId -> userStorage.getUserById(friendId))
                .toList();
    }
}

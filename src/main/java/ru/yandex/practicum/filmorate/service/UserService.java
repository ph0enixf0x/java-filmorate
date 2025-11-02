package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserStorage userStorage;

    public void addFriend(int userId, int friendId) {
        User originalUser = userStorage.getUserById(userId);
        User friendUser = userStorage.getUserById(friendId);
        originalUser.getFriends().add(friendId);
        friendUser.getFriends().add(userId);
    }

    public void removeFriend(int userId, int friendId) {
        User originalUser = userStorage.getUserById(userId);
        User friendUser = userStorage.getUserById(friendId);
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

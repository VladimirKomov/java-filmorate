package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.Storage;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService extends AbstractService<User> {

    final FriendStorage friendStorage;

    @Autowired
    public UserService(Storage<User> storage, FriendStorage friendStorage) {
        this.storage = storage;
        this.friendStorage = friendStorage;
    }


    @Override
    protected void validate(User data) {
        if (data.getLogin() == null || data.getLogin().isBlank()) {
            throw new ValidationException("Login invalid");
        }
        if (data.getEmail() == null || data.getEmail().isBlank() || !data.getEmail().contains("@")) {
            throw new ValidationException("E-mail invalid");
        }
        if (data.getName() == null || data.getName().isBlank()) {
            data.setName(data.getLogin());
        }
        if (data.getBirthday() == null || data.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Birthday invalid");
        }

    }

    public void addFriend(long userId, long friendId) {
        final User user = storage.get(userId);
        final User friend = storage.get(friendId);
        friendStorage.addFriend(user.getId(), friend.getId());
    }

    public void removeFriend(long userId, long friendId) {
        final User user = storage.get(userId);
        final User friend = storage.get(friendId);
        storage.get(friendId);
        friendStorage.removeFriend(user.getId(), friend.getId());
    }

    public List<User> getFriends(long id) {
        final User user = storage.get(id);
        return friendStorage.getFriends(user.getId());
    }

    public List<User> getCommonFriends(long id, long otherId) {
        final User user = storage.get(id);
        final User other = storage.get(otherId);

        return friendStorage.getCommonFriends(user.getId(), other.getId());
    }

}

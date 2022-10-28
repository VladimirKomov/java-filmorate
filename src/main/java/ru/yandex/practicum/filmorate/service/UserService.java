package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService extends AbstractService<User> {

    @Autowired
    public UserService(Storage<User> storage) {
        this.storage = storage;
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
        user.getFriendsId().add(friendId);
        friend.getFriendsId().add(userId);
    }

    public void removeFriend(long userId, long friendId) {
        final User user = storage.get(userId);
        final User friend = storage.get(friendId);
        storage.get(friendId);
        user.getFriendsId().remove(friendId);
        friend.getFriendsId().remove(userId);
    }

    public List<User> getFriends(long id) {
        return getUsersById(new ArrayList<>(storage.get(id).getFriendsId()));
    }

    public List<User> getCommonFriends(long id, long otherId) {
        Set<Long> friendsId = storage.get(id).getFriendsId();
        Set<Long> otherFriendsId = storage.get(otherId).getFriendsId();

        List<Long> commonFriendsId = friendsId.stream()
                .filter(otherFriendsId :: contains)
                .collect(Collectors.toList());
        return getUsersById(commonFriendsId);
    }

    private List<User> getUsersById(List<Long> usersId) {
        List<User> users = new ArrayList<>();
        for (long id : usersId) {
            users.add(storage.get(id));
        }
        return users;
    }
}

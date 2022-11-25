package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //создание пользователя
    @PostMapping
    public User post(@Valid @RequestBody User user) {
        log.info("Сохраняется {}", user.toString());

        return userService.create(user);
    }

    //обновление пользователя
    @PutMapping
    public User put(@Valid @RequestBody User user) {
        log.info("Сохраняется {}", user.toString());

        return userService.update(user);
    }

    //получение списка всех пользователей
    @GetMapping
    public List<User> getAll() {
        log.info("Полвчение всех Пользователей");

        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        log.info("Получение Пользователя id={}", id);

        return userService.get(id);
    }

    @DeleteMapping
    public void deleteAll() {
        log.info("Удаляются все Пользователи");

        userService.deleteAll();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void put(@PathVariable long id, @PathVariable long friendId) {
        log.info("Сохраняюся пользователю id={} друзья friendId={}  ", id, friendId);

        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteAll(@PathVariable long id, @PathVariable long friendId) {
        log.info("Удаляются Пользователю id={} друзья friendId={}", id, friendId);

        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        log.info("Друзья пользователя id={}", id);

        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        log.info("Друзья пользователя id={} общие с пользоватлем otherId={}", id, otherId);

        return userService.getCommonFriends(id, otherId);
    }

}

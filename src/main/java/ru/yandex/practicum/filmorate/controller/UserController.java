package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int generateId = 0;

    //создание пользователя
    @PostMapping
    public User post(@Valid @RequestBody User user) {
        log.debug("Получен запрос POST /users.");
        log.info("Сохраняется {}", user.toString());
        user.setId(++generateId);
        validateName(user);
        users.put(user.getId(), user);

        return user;
    }

    //обновление пользователя
    @PutMapping
    public User put(@Valid @RequestBody User user) {
        log.debug("Получен запрос PUT /users.");
        log.info("Сохраняется {}", user.toString());
        validateName(user);
        if (users.containsValue(user)) {
            throw new AlreadyExistException("Пользователь c таким E-mail существует");
        }
        if (user.getId() == 0) {
            user.setId(++generateId);
        }
        users.put(user.getId(), user);

        return user;
    }

    //получение списка всех пользователей
    @GetMapping
    public List<User> getAll() {
        log.debug("Получен запрос GET /users.");
        log.info("Пользователей {}", users.size());

        return users.entrySet()
                .stream()
                .map(e -> e.getValue())
                .collect(Collectors.toList());
    }

    private void validateName(User user) {
        if (!StringUtils.hasText(user.getName())) {
            log.warn("имя для отображения может быть пустым — в таком случае будет использован логин");
            user.setName(user.getLogin());
        }
    }

}

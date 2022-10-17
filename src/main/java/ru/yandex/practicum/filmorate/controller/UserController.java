package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

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
        //users.put(user, user.getId());

        return user;
    }

    //обновление пользователя
    @PutMapping
    public User put(@Valid @RequestBody User user) {
        log.debug("Получен запрос PUT /users.");
        log.info("Сохраняется {}", user.toString());
        validateName(user);
        //переделал обновление позователей
        if (users.containsValue(user)) {
            Optional<Integer> key = users.entrySet()
                    .stream()
                    .filter(entry -> user.equals(entry.getValue()))
                    .map(Map.Entry::getKey)
                    .findFirst();
            users.replace(key.get(), user);
        } else {
            if (user.getId() == 0) {
                user.setId(++generateId);
            }
            users.put(user.getId(), user);
        }

        return user;
    }

    //получение списка всех пользователей
    @GetMapping
    public List<User> getAll() {
        log.debug("Получен запрос GET /users.");
        log.info("Пользователей {}", users.size());

        return new ArrayList<>(users.values());
    }

    //в ТЗ удаления не было, будет отсебячина
    @DeleteMapping
    public void deleteAll() {
        log.debug("Получен запрос DELETE /users.");
        log.info("Удалено Пользователей {}", users.size());

        users.clear();
    }

    private void validateName(User user) {
        if (!StringUtils.hasText(user.getName())) {
            log.warn("имя для отображения может быть пустым — в таком случае будет использован логин");
            user.setName(user.getLogin());
        }
    }

}

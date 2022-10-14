package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private int generateId = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    //добавление фильма
    @PostMapping
    public Film film(@Valid @RequestBody Film film) {
        log.debug("Получен запрос POST /films.");
        log.info("Сохраняется {}", film.toString());
        film.setId(++generateId);
        films.put(film.getId(), film);

        return film;
    }

    //обновление фильма
    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        log.debug("Получен запрос PUT /films.");
        log.info("Сохраняется {}", film.toString());
        if (films.containsValue(film)) {
            throw new AlreadyExistException("Фильм уже добавлен");
        }
        if (film.getId() == 0) {
            film.setId(++generateId);
        }
        films.put(film.getId(), film);

        return film;
    }

    //получение всех фильмов
    @GetMapping
    public List<Film> getAll() {
        log.debug("Получен запрос GET /films.");
        log.info("Фильмов {}", films.size());

        return films.entrySet()
                .stream()
                .map(e -> e.getValue())
                .collect(Collectors.toList());
    }

}

package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.*;
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
            Optional<Integer> key = Optional.empty();
            for (Map.Entry<Integer, Film> entry : films.entrySet())
                if (films.equals(entry.getValue())) {
                    Integer integerFilmEntryKey = entry.getKey();
                    key = Optional.of(integerFilmEntryKey);
                    break;
                }
            films.replace(key.get(), film);
        } else {
            if (film.getId() == 0) {
                film.setId(++generateId);
            }
            films.put(film.getId(), film);
        }

        return film;
    }

    //получение всех фильмов
    @GetMapping
    public List<Film> getAll() {
        log.debug("Получен запрос GET /films.");
        log.info("Фильмов {}", films.size());

        return new ArrayList<>(films.values());
    }

    //в ТЗ удаления не было, будет отсебячина
    @DeleteMapping
    public void deleteAll() {
        log.debug("Получен запрос DELETE /films.");
        log.info("Удалено Фильмов {}", films.size());

        films.clear();
    }

}

package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;


@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    //добавление фильма
    @PostMapping
    public Film post(@Valid @RequestBody Film film) {
        log.info("Сохраняется {}", film.toString());

        return filmService.create(film);
    }

    //обновление фильма
    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        log.info("Сохраняется {}", film.toString());

        return filmService.update(film);
    }

    //получение всех фильмов
    @GetMapping
    public List<Film> getAll() {
        final List<Film> films = filmService.getAll();
        log.info("Фильмов {}", films.size());

        return films;
    }

    @GetMapping("/{id}")
    public Film get(@PathVariable long id) {
        log.info("GET Фильм id={}", id);

        return filmService.get(id);
    }

    @DeleteMapping
    public void deleteAll() {
        log.info("Удалено Фильмов {}", filmService.getSize());

        filmService.deleteAll();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        log.info("DELETE Фильм id={}", id);

        filmService.delete(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Добавляетсвя лайк к фильму id={} пользователем userId={}", id, userId);

        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Удаляется лайк к фильму id={} пользователем userId={}", id, userId);

        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        log.info("Получаем популярные фильмы ={}", count);

        return filmService.getPopular(count);
    }

}

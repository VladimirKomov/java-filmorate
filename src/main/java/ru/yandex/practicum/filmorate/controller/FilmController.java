package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;


@Slf4j
@RestController
@RequestMapping()
public class FilmController {

    FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    //добавление фильма
    @PostMapping("/films")
    public Film post(@Valid @RequestBody Film film) {
        log.info("Сохраняется {}", film.toString());
        filmService.create(film);

        return filmService.get(film.getId());
    }

    //обновление фильма
    @PutMapping("/films")
    public Film put(@Valid @RequestBody Film film) {
        log.info("Сохраняется {}", film.toString());

        return filmService.update(film);
    }

    //получение всех фильмов
    @GetMapping("/films")
    public List<Film> getAll() {
        final List<Film> films = filmService.getAll();
        log.info("Фильмов {}", films.size());

        return films;
    }

    @GetMapping("/films/{id}")
    public Film get(@PathVariable long id) {
        log.info("GET Фильм id={}", id);

        return filmService.get(id);
    }

    @DeleteMapping("/films")
    public void deleteAll() {
        log.info("Удаляются все Фильмы");

        filmService.deleteAll();
    }

    @DeleteMapping("/films/{id}")
    public void deleteById(@PathVariable long id) {
        log.info("DELETE Фильм id={}", id);

        filmService.delete(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Добавляетсвя лайк к фильму id={} пользователем userId={}", id, userId);

        filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Удаляется лайк к фильму id={} пользователем userId={}", id, userId);

        filmService.removeLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        log.info("Получаем популярные фильмы ={}", count);

        return filmService.getPopular(count);
    }

    @GetMapping("/mpa")
    public List<Mpa> getAllMpa() {
        final List<Mpa> mpaList = filmService.getAllMpa();
        log.info("MPA {}", mpaList.size());

        return mpaList;
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMpaById(@PathVariable long id) {
        log.info("GET MPA id={}", id);

        return filmService.getMpa(id);
    }

    @GetMapping("/genres")
    public List<Genre> getAllGenres() {
        final List<Genre> genres = filmService.getAllGenres();
        log.info("Genres {}", genres.size());

        return genres;
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable long id) {
        log.info("GET Genre id={}", id);

        return filmService.getGenre(id);
    }


}

package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.Storage;

import javax.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;

import static ru.yandex.practicum.filmorate.Constants.DATE_FIRST_FILM;
import static ru.yandex.practicum.filmorate.Constants.MAX_FILM_DESCRIPTION_SIZE;

@Service
public class FilmService extends AbstractService<Film> {

    final Storage<User> userStorage;
    final LikeStorage likeStorage;
    final GenreStorage genreStorage;

    final MpaStorage mpaStorage;

    @Autowired
    public FilmService(Storage<Film> storage, Storage<User> userStorage, LikeStorage likeStorage,
                       GenreStorage genreStorage, MpaStorage mpaStorage) {
        this.storage = storage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
    }

    @Override
    protected void validate(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("Name invalid");
        }
        if (film.getDescription() != null && film.getDescription().length() > MAX_FILM_DESCRIPTION_SIZE) {
            throw new ValidationException("Description invalid");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(DATE_FIRST_FILM)) {
            throw new ValidationException("Date invalid");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Duration invalid");
        }
    }

    public void addLike(long filmId, long userId) {
        final Film film = storage.get(filmId);
        final User user = userStorage.get(userId);
        likeStorage.addLike(film.getId(), user.getId());
    }

    public void removeLike(long filmId, long userId) {
        final Film film = storage.get(filmId);
        final User user = userStorage.get(userId);
        likeStorage.removeLike(film.getId(), user.getId());
    }

    public List<Film> getPopular(int size) {
        //return likeStorage.getPopular(size);
        //тут хуета
        List<Film> films = likeStorage.getPopular(size);
        genreStorage.load(films);
        return films;
    }
    @Override
    public List<Film> getAll() {
        final List<Film> films = super.getAll();
        genreStorage.load(films);
        return films;
    }

    public Film save(Film film) {
        storage.create(film);
        return film;
    }

    public Film update(Film film) {
        final  Film filmForUpdate = storage.get(film.getId());
        validate(film);
        film.setRate(filmForUpdate.getRate());
        storage.update(film);

        return get(film.getId());
    }

    @Override
    public Film get(long id) {
        final  Film film = storage.get(id);
        List<Film> films = new ArrayList<>();
        films.add(film);
        genreStorage.load(films);
        return film;
    }

    public List<Mpa> getAllMpa() {
        return mpaStorage.getAll();
    }

    public Mpa getMpa(long id) {
        return mpaStorage.get(id);
     }

    public List<Genre> getAllGenres() {
        return genreStorage.getAll();
    }

    public Genre getGenre(long id) {
        return genreStorage.get(id);
    }

}

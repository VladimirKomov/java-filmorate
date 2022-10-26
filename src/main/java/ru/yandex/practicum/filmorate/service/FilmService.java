package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import javax.validation.ValidationException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.Constants.DATE_FIRST_FILM;
import static ru.yandex.practicum.filmorate.Constants.MAX_FILM_DESCRIPTION_SIZE;

@Service
public class FilmService extends AbstractService<Film> {

    final Storage<User> userStorage;

    @Autowired
    public FilmService(Storage<Film> storage, Storage<User> userStorage) {
        this.storage = storage;
        this.userStorage = userStorage;
    }

    @Override
    protected void validate(Film data) {
        if (data.getName() == null || data.getName().isEmpty()) {
            throw new ValidationException("Name invalid");
        }
        if (data.getDescription() != null && data.getDescription().length() > MAX_FILM_DESCRIPTION_SIZE) {
            throw new ValidationException("Description invalid");
        }
        if (data.getReleaseDate() == null || data.getReleaseDate().isBefore(DATE_FIRST_FILM)) {
            throw new ValidationException("Date invalid");
        }
        if (data.getDuration() <= 0) {
            throw new ValidationException("Duration invalid");
        }
    }

    public void addLike(long id, long userId) {
        final Film film = storage.get(id);
        userStorage.get(userId);
        film.addLike(userId);
    }

    public void removeLike(long id, long userId) {
        final Film film = storage.get(id);
        userStorage.get(userId);
        film.removeLike(userId);
    }

    public List<Film> getPopular(int size) {
        return storage.getAll().stream()
                .sorted(Comparator.comparing(f -> -f.getRate()))
                .limit(size)
                .collect(Collectors.toList());
    }

}

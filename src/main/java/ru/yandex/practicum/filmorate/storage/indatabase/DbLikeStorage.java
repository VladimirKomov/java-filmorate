package ru.yandex.practicum.filmorate.storage.indatabase;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.List;

@Component
public class DbLikeStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    public DbLikeStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(long filmId, long userId) {
        String sqlQuery = "merge into likes (user_id, film_id) values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, filmId);
        updateRateFilm(filmId);
    }

    private void updateRateFilm(long filmId) {
        String sqlQuery = "update films set rate = (select count(film_id) from likes " +
                "where likes.film_id = films.id) where films.id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        String sqlQuery = "delete from likes where user_id = ? and film_id = ?";
        jdbcTemplate.update(sqlQuery, userId, filmId);
        updateRateFilm(filmId);
    }

    @Override
    public List<Film> getPopular(int count) {
        String sqlQuery = "select * " +
                "from FILMS LEFT JOIN " +
                "MPA where FILMS.MPA_ID = MPA.ID " +
                "order by FILMS.RATE desc " +
                "limit ?";
        return jdbcTemplate.query(sqlQuery, DbFilmStorage::makeFilm, count);
    }

}

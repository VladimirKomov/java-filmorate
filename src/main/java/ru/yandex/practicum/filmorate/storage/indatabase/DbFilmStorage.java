package ru.yandex.practicum.filmorate.storage.indatabase;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Primary
@Component
public class DbFilmStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper = new FilmMapper();


    public DbFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void create(Film film) {
        String sqlQuery = "insert into FILMS (NAME, DESCRIPTION, RELEASE_DATE, RATE, DURATION, MPA_ID) " +
                "values (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"id"});
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setDate(3, Date.valueOf(film.getReleaseDate()));
            statement.setLong(4, film.getRate());
            statement.setLong(5, film.getDuration());
            statement.setLong(6, film.getMpa().getId());
            return statement;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());

        saveGenres(film);

    }

    private void saveGenres(Film film) {
        final Long filmId = film.getId();
        String sqlQueryDelete = "delete from films_genres where film_id = ?";
        jdbcTemplate.update(sqlQueryDelete, filmId);

        List<Long> genresList = film.getGenres().stream()
                .map(Genre::getId)
                .distinct()
                .collect(Collectors.toList());

        if (genresList.isEmpty()) {
            return;
        }

        String sqlQueryInsert = "insert into films_genres (film_id, genre_id) values (?,?)";

        //переделать на batchUpdate
        //genresList.forEach(e -> jdbcTemplate.update(sqlQueryInsert, filmId, e));

        jdbcTemplate.batchUpdate(sqlQueryInsert,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, filmId);
                        ps.setLong(2, genresList.get(i));
                    }

                    @Override
                    public int getBatchSize() {
                        return genresList.size();
                    }
                }
        );
    }

    @Override
    public void update(Film film) {
        String sqlQuery = "update films set name = ?, description = ?, release_date = ?, rate = ?, " +
                "duration = ?, mpa_id = ? where id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getRate(), film.getDuration(), film.getMpa().getId(), film.getId());

        saveGenres(film);
    }

    @Override
    public Film get(long id) {
        String sqlQuery = "select * from FILMS LEFT JOIN " +
                "MPA where FILMS.MPA_ID = MPA.ID AND FILMS.ID = ?";

        final List<Film> films = jdbcTemplate.query(sqlQuery, filmMapper, id);
        if (films.size() != 1) {
            throw new DataNotFoundException("Фильм id=" + id);
        }
        return films.get(0);
    }

    @Override
    public void delete(long id) {
        String sqlQuery = "delete from films where id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public void deleteAll() {
        String sqlQuery = "delete from films";
        jdbcTemplate.update(sqlQuery);
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "select * from FILMS LEFT JOIN " +
                "MPA where FILMS.MPA_ID = MPA.ID";
        return jdbcTemplate.query(sqlQuery, filmMapper);
    }

}

package ru.yandex.practicum.filmorate.storage.indatabase;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.BaseModel;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

@Component
public class DbGenreStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public DbGenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Genre get(long id) {
        String sqlQuery = "select * from genres where id = ?";
        final List<Genre> genres = jdbcTemplate.query(sqlQuery, this :: makeGenre, id);
        if (genres.size() !=1) {
            throw new DataNotFoundException("Жанр id=" + id);
        }
        return genres.get(0);
    }

    private Genre makeGenre(ResultSet rs, int row) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getLong("id"));
        genre.setName(rs.getString("name"));

        return genre;
    }

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "select * from genres";
        return jdbcTemplate.query(sqlQuery, this :: makeGenre);
    }

    @Override
    public void load(List<Film> films) {
        String inSQL = String.join(",", Collections.nCopies(films.size(), "?"));
        Map<Long, Film> filmsBuId = films.stream().collect(Collectors.toMap(BaseModel::getId, f -> f));
        String sqlQuery = "select * from GENRES g left join " +
                "FILMS_GENRES fg where fg.GENRE_ID = g.ID and fg.FILM_ID in (" +  inSQL + ")" +
                " ORDER BY g.ID";
        jdbcTemplate.query(sqlQuery,
                rs -> {
                Film film = filmsBuId.get(rs.getLong("film_id"));
                film.addGenre(makeGenre(rs,0));
                }, films.stream().map(BaseModel::getId).toArray());
    }
}

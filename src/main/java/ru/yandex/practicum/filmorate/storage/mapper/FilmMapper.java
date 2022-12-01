package ru.yandex.practicum.filmorate.storage.mapper;


import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;


public class FilmMapper implements RowMapper {

    @Override
    public Object mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("films.id"));
        film.setName(rs.getString("films.name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getLong("duration"));
        film.setRate(rs.getLong("rate"));
        Mpa mpa = new Mpa();
        mpa.setId(rs.getLong("mpa.id"));
        mpa.setName(rs.getString("mpa.name"));
        film.setMpa(mpa);
        return film;
    }
}

package ru.yandex.practicum.filmorate.storage.indatabase;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Component
public class DbMpaStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper mpaRowMapper = new BeanPropertyRowMapper<>(Mpa.class);

    public DbMpaStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa get(long id) {
        String sqlQuery = "select * from mpa where id = ?";
        final List<Mpa> mpaList = jdbcTemplate.query(sqlQuery, mpaRowMapper, id);
        if (mpaList.size() !=1) {
            throw new DataNotFoundException("Фильм id=" + id);
        }
        return mpaList.get(0);
    }

    @Override
    public List<Mpa> getAll() {
        String sqlQuery = "select * from mpa";
        return jdbcTemplate.query(sqlQuery, mpaRowMapper);
    }

}

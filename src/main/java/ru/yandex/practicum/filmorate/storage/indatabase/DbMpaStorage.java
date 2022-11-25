package ru.yandex.practicum.filmorate.storage.indatabase;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class DbMpaStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public DbMpaStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Mpa get(long id) {
        String sqlQuery = "select * from mpa where id = ?";
        final List<Mpa> mpaList = jdbcTemplate.query(sqlQuery, this::makeMpa, id);
        if (mpaList.size() !=1) {
            throw new DataNotFoundException("Фильм id=" + id);
        }
        return mpaList.get(0);
    }

    @Override
    public List<Mpa> getAll() {
        String sqlQuery = "select * from mpa";
        return jdbcTemplate.query(sqlQuery, this::makeMpa);
    }

    private Mpa makeMpa(ResultSet rs, int i) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(rs.getLong("id"));
        mpa.setName(rs.getString("name"));
        return mpa;
    }
}

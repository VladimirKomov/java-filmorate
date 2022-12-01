package ru.yandex.practicum.filmorate.storage.indatabase;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Primary
@Component
public class DbUserStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper userRowMapper = new BeanPropertyRowMapper<>(User.class);


    public DbUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(User user) {

        String sqlQuery = "insert into users (email, login, name, birthday) " +
                "values (?, ?, ?, ?)";

        //без ключа, добавлен ключ
        //jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(),
        //       Date.valueOf(user.getBirthday()));

        //получение ключа он вроде как и не нужен

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"id"});
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getName());
            statement.setDate(4, Date.valueOf(user.getBirthday()));
            return statement;
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void update(User user) {
        String sqlQuery = "update users set email = ?, login = ?, name = ?, birthday = ? where id = ?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(),
                user.getBirthday(), user.getId());
    }

    @Override
    public User get(long id) {
        String sqlQuery = "select * from users where id = ?";
        final List<User> users = jdbcTemplate.query(sqlQuery,userRowMapper , id);
        if (users.size() != 1) {
            throw new DataNotFoundException("Пользователь id=" + id);
        }
        return users.get(0);
    }

    @Override
    public void delete(long id) {
        String sqlQuery = "delete from users where id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public void deleteAll() {
        String sqlQuery = "delete from users";
        jdbcTemplate.update(sqlQuery);
    }

    @Override
    public List<User> getAll() {
        String sqlQuery = "select * from users";
        return jdbcTemplate.query(sqlQuery, userRowMapper);
    }

}

package ru.yandex.practicum.filmorate.storage.indatabase;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;


import java.util.List;

@Component
public class DbFriendStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper userRowMapper = new BeanPropertyRowMapper<>(User.class);

    public DbFriendStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(long userId, long friendId) {
        String sqlQuery = "merge into friends (user_id, friend_id) values (?,?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        String sqlQuery = "delete from friends where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public List<User> getFriends(long userId) {
        String sqlQuery = "select * from USERS left join FRIENDS " +
                "where USERS.ID = FRIENDS.FRIEND_ID and FRIENDS.USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, userRowMapper, userId);

    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        String sqlQuery = "select * from USERS left join FRIENDS " +
                "where USERS.ID = FRIENDS.FRIEND_ID and USER_ID = ? and FRIEND_ID in " +
                "(select FRIEND_ID from FRIENDS where USER_ID = ?)";
        return jdbcTemplate.query(sqlQuery, userRowMapper, userId, friendId);
    }
}

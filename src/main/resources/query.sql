----сервисное
--удаление таблиц
drop all objects;

--очистка таблиц
delete from FRIENDS;
delete from LIKES;
delete from FILMS_GENRES;
delete from LIKES;
delete from USERS;
delete from FILMS;

---- проверка запросов
-- получение фильмов фильмов
select * from FILMS LEFT JOIN MPA where FILMS.MPA_ID = MPA.ID AND FILMS.ID = ?;

-- получение жанров фильма (по порядку)
select * from GENRES g left join FILMS_GENRES fg where fg.GENRE_ID = g.ID and fg.FILM_ID in (?) ORDER BY g.ID;

select *
from FILMS LEFT JOIN
     MPA where FILMS.MPA_ID = MPA.ID;

select *
from FILMS LEFT JOIN
     MPA where FILMS.MPA_ID = MPA.ID AND FILMS.ID = ?;

select * from genres where id = ?;

select * from GENRES g left join
    FILMS_GENRES fg where fg.GENRE_ID = g.ID and fg.FILM_ID in (?);

select *
from FILMS LEFT JOIN
    MPA where FILMS.MPA_ID = MPA.ID
    order by FILMS.RATE desc
    limit ?;

update users set email = ?, login = ?, name = ?, birthday = ? where id = ?;

select * from FRIENDS;

-- получение друзей позапросом
select * from USERS where ID = (select FRIEND_ID from FRIENDS where USER_ID = ?);

-- получение друзей соединением
select * from USERS left join FRIENDS where USERS.ID = FRIENDS.FRIEND_ID and FRIENDS.USER_ID = ?;

-- общие друзья
select * from USERS left join FRIENDS where USERS.ID = FRIENDS.FRIEND_ID and USER_ID = ? and FRIEND_ID in (select FRIEND_ID from FRIENDS where USER_ID = ?);

-- все пользователи
select *
from USERS;

--лайки
update films set rate = (select count(film_id) from likes where likes.film_id = films.id) where films.id = ?;

--размер таблицы
select count(ID) as result from USERS;

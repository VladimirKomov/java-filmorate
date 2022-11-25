--очищаем перед стартом
drop all objects;

-- создаем таблицы после очистки
create table if not exists USERS
(
    id int not null primary key auto_increment,
    email varchar(255) not null,
    login varchar(255) not null,
    name varchar(255) not null,
    birthday date,
    UNIQUE (email),
    UNIQUE (login)
);

create table if not exists MPA
(
    id int not null primary key auto_increment,
    name varchar(255)
);

create table if not exists GENRES
(
    id int not null primary key auto_increment,
    name varchar(255)
);

create table if not exists FILMS
(
    id int not null primary key auto_increment,
    name varchar(255),
    description varchar(255),
    release_date date,
    rate int,
    duration int,
    mpa_id int references MPA (id)
);

create table if not exists GENRES
(
    id int not null primary key auto_increment,
    name varchar(255)
);

create table if not exists FILMS_GENRES
(
    film_id int references FILMS (id) on DELETE cascade,
    genre_id int references GENRES(id) on DELETE cascade,
    PRIMARY KEY (film_id, genre_id)
);

create table if not exists FRIENDS
(
    user_id int not null references USERS(id) on DELETE cascade,
    friend_id int not null references USERS(id) on DELETE cascade,
    primary key (user_id, friend_id)
);

create table if not exists LIKES
(
    user_id int references USERS (id) on DELETE cascade,
    film_id int references FILMS (id) on DELETE cascade,
    PRIMARY KEY (user_id, film_id)
);

--первоначальное заполнение
merge into MPA (ID, NAME)
    values
        (1, 'G'),
        (2, 'PG'),
        (3, 'PG-13'),
        (4, 'R'),
        (5, 'NC-17');

merge into GENRES (id, name)
    values
        (1, 'Комедия'),
        (2, 'Драма'),
        (3, 'Мультфильм'),
        (4, 'Триллер'),
        (5, 'Документальный'),
        (6, 'Боевик');

-- Для тестирвоания, потом удалю

-- merge into FILMS (ID, NAME, DESCRIPTION, RELEASE_DATE, RATE, DURATION, MPA_ID)
-- values
--     (1, 'film1', 'bla vla bla', '2000-01-01', 1, 90, 1),
--     (2, 'film2', 'bla vla bla', '2001-01-01', 2, 100, 2),
--     (3, 'film3', 'bla vla bla', '2002-01-01', 3, 110, 3);
--
-- merge into FILMS_GENRES (film_id, genre_id)
-- values
--     (1, 1),
--     (2, 2),
--     (3, 3);
--
-- merge into USERS (ID, EMAIL, LOGIN, NAME, BIRTHDAY)
--     values
--         (1, 'mail@mail.ru', 'user1', 'Nick Name 1', '2000-01-01' ),
--         (2, 'yandex@mail.ru', 'user2', 'Nick Name 2', '2001-01-01'),
--         (3, 'test@mail.ru', 'user3', 'Nick Name 3', '2002-01-01');
--
-- merge into FRIENDS (user_id, friend_id)
-- values
--     (1, 2),
--     (2, 1),
--     (1, 3);
--
-- merge into LIKES (USER_ID, FILM_ID)
-- values
--     (1, 2),
--     (2, 3),
--     (3, 1),
--     (2, 1);








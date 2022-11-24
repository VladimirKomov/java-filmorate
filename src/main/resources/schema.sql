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
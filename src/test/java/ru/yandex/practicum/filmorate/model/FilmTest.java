package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmTest {

    private static Validator validator;
    @BeforeEach
    void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    //название не может быть пустым
    //максимальная длина описания — 200 символов
    //дата релиза — не раньше 28 декабря 1895 года
    //продолжительность фильма должна быть положительной

    @Test
    void filmGood() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000,01,01))
                .duration(120)
                .build();

        Set<ConstraintViolation<Film>> validates = validator.validate(film);
        assertEquals(0, validates.size(), "Не верное количество фильмов");
    }

    @Test
    void filNameIsNull() {
        Film film = Film.builder()
                .name(null)
                .description("Description")
                .releaseDate(LocalDate.of(2000,01,01))
                .duration(120)
                .build();

        Set<ConstraintViolation<Film>> validates = validator.validate(film);
        assertEquals(1, validates.size(), "Не верное количество фильмов");
    }

    @Test
    void filNameIsBlank() {
        Film film = Film.builder()
                .name("")
                .description("Description")
                .releaseDate(LocalDate.of(2000,01,01))
                .duration(120)
                .build();

        Set<ConstraintViolation<Film>> validates = validator.validate(film);
        assertEquals(1, validates.size(), "Не верное количество фильмов");
    }

    @Test
    void filNameIsEmpty() {
        Film film = Film.builder()
                .name(" ")
                .description("Description")
                .releaseDate(LocalDate.of(2000,01,01))
                .duration(120)
                .build();

        Set<ConstraintViolation<Film>> validates = validator.validate(film);
        assertEquals(1, validates.size(), "Не верное количество фильмов");
    }

    @Test
    void filDescription201() {
        Film film = Film.builder()
                .name("Film")
                .description("01234567890123456789012345678901234567890123456789"
                        + "01234567890123456789012345678901234567890123456789"
                        + "01234567890123456789012345678901234567890123456789"
                        + "01234567890123456789012345678901234567890123456789"
                        + "0")
                .releaseDate(LocalDate.of(2000,01,01))
                .duration(120)
                .build();

        Set<ConstraintViolation<Film>> validates = validator.validate(film);
        assertEquals(1, validates.size(), "Не верное количество фильмов");
    }

    @Test
    void filReleaseDate18851227() {
        Film film = Film.builder()
                .name("Film")
                .description("description")
                .releaseDate(LocalDate.of(1885,12,27))
                .duration(120)
                .build();

        Set<ConstraintViolation<Film>> validates = validator.validate(film);
        assertEquals(1, validates.size(), "Не верное количество фильмов");
    }

    @Test
    void filDuration0() {
        Film film = Film.builder()
                .name("Film")
                .description("description")
                .releaseDate(LocalDate.of(2000,01,01))
                .duration(0)
                .build();

        Set<ConstraintViolation<Film>> validates = validator.validate(film);
        assertEquals(1, validates.size(), "Не верное количество фильмов");
    }

    @Test
    void filDurationNegative() {
        Film film = Film.builder()
                .name("Film")
                .description("description")
                .releaseDate(LocalDate.of(2000,01,01))
                .duration(-1)
                .build();

        Set<ConstraintViolation<Film>> validates = validator.validate(film);
        assertEquals(1, validates.size(), "Не верное количество фильмов");
    }


}
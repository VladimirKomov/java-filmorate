package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

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

    private static Stream<Arguments> generate() {
        //Film goodFilm = new Film(1,"Film", "Description")

        return Stream.of(
                //good film
                Arguments.of(Film.builder()
                        .name("Film")
                        .description("Description")
                        .releaseDate(LocalDate.of(2000, 01, 01))
                        .duration(120L)
                        .build(), 0),
                //name null
                Arguments.of(Film.builder()
                        .name(null)
                        .description("Description")
                        .releaseDate(LocalDate.of(2000, 01, 01))
                        .duration(120L)
                        .build(), 1),
                //name blink
                Arguments.of(Film.builder()
                        .name("")
                        .description("Description")
                        .releaseDate(LocalDate.of(2000, 01, 01))
                        .duration(120L)
                        .build(), 1),
                //name blink
                Arguments.of(Film.builder()
                        .name(" ")
                        .description("Description")
                        .releaseDate(LocalDate.of(2000, 01, 01))
                        .duration(120L)
                        .build(), 1),
                //Description size 201
                Arguments.of(Film.builder()
                        .name("Film")
                        .description("01234567890123456789012345678901234567890123456789"
                                + "01234567890123456789012345678901234567890123456789"
                                + "01234567890123456789012345678901234567890123456789"
                                + "01234567890123456789012345678901234567890123456789"
                                + "0")
                        .releaseDate(LocalDate.of(2000, 01, 01))
                        .duration(120L)
                        .build(), 1),
                //releaseDate 27.12.1885
                Arguments.of(Film.builder()
                        .name("Film")
                        .description("description")
                        .releaseDate(LocalDate.of(1885, 12, 27))
                        .duration(120L)
                        .build(), 1),
                //duration 0
                Arguments.of(Film.builder()
                        .name("Film")
                        .description("description")
                        .releaseDate(LocalDate.of(2000, 01, 01))
                        .duration(0L)
                        .build(), 1),
                //duration negative
                Arguments.of(Film.builder()
                        .name("Film")
                        .description("description")
                        .releaseDate(LocalDate.of(2000, 01, 01))
                        .duration(-1L)
                        .build(), 1));
    }

    @ParameterizedTest
    @MethodSource("generate")
    void filmTests(Film film, int exp) {

        Set<ConstraintViolation<Film>> validates = validator.validate(film);
        assertEquals(exp, validates.size(), "Не верное количество фильмов");
    }

}
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserTest {

    private static Validator validator;
    @BeforeEach
    void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    //электронная почта не может быть пустой и должна содержать символ @
    //логин не может быть пустым и содержать пробелы
    //имя для отображения может быть пустым — в таком случае будет использован логин
    //дата рождения не может быть в будущем

    private static Stream<Arguments> generate() {

        return Stream.of(
                //good user
                Arguments.of(User.builder()
                        .id(1)
                        .email("mail@yandex.ru")
                        .login("login")
                        .name("name")
                        .birthday(LocalDate.of(2000,01,01))
                        .build(), 0),
                //email null
                Arguments.of(User.builder()
                        .id(1)
                        .email(null)
                        .login("login")
                        .name("name")
                        .birthday(LocalDate.of(2000,01,01))
                        .build(), 1),
                //email blink
                Arguments.of(User.builder()
                        .id(1)
                        .email("")
                        .login("login")
                        .name("name")
                        .birthday(LocalDate.of(2000,01,01))
                        .build(), 1),
                //email empty
                Arguments.of(User.builder()
                        .id(1)
                        .email(" ")
                        .login("login")
                        .name("name")
                        .birthday(LocalDate.of(2000,01,01))
                        .build(), 2),
                //email without commercial at
                Arguments.of(User.builder()
                        .id(1)
                        .email("mail.yandex.ru")
                        .login("login")
                        .name("name")
                        .birthday(LocalDate.of(2000,01,01))
                        .build(), 1),
                //login blink
                Arguments.of(User.builder()
                        .id(1)
                        .email("mail@yandex.ru")
                        .login("")
                        .name("name")
                        .birthday(LocalDate.of(2000,01,01))
                        .build(), 1),
                //login empty
                Arguments.of(User.builder()
                        .id(1)
                        .email("mail@yandex.ru")
                        .login(" ")
                        .name("name")
                        .birthday(LocalDate.of(2000,01,01))
                        .build(), 1),
                //birthday after now
                Arguments.of(User.builder()
                        .id(1)
                        .email("mail@yandex.ru")
                        .login("login")
                        .name("name")
                        .birthday(LocalDate.of(2100,01,01))
                        .build(), 1));
    }

    @ParameterizedTest
    @MethodSource("generate")
    void userTests(User user, int exp) {
        Set<ConstraintViolation<User>> validates = validator.validate(user);
        assertEquals(exp, validates.size(), "Не верное количество пользователей");
    }

}
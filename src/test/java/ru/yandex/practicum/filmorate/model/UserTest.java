package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;


import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void userGood() {
        //User user = new User(1, "mail@yandex.ru", "login", "name", LocalDate.of(2000,01,01));
        User user = User.builder()
                .id(1)
                .email("mail@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2000,01,01))
                .build();

        Set<ConstraintViolation<User>> validates = validator.validate(user);
        assertEquals(0, validates.size(), "Не верное количество пользователей");

    }

    @Test
    void userEmailNull() {
        User user = User.builder()
                .id(1)
                .email(null)
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2000,01,01))
                .build();

        Set<ConstraintViolation<User>> validates = validator.validate(user);
        assertEquals(1, validates.size(), "Не верное количество пользователей");

    }

    @Test
    void userEmailIsBlink() {
        User user = User.builder()
                .id(1)
                .email("")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2000,01,01))
                .build();

        Set<ConstraintViolation<User>> validates = validator.validate(user);
        assertEquals(1, validates.size(), "Не верное количество пользователей");

    }

    @Test
    void userEmailIsEmpty() {
        User user = User.builder()
                .id(1)
                .email(" ")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2000,01,01))
                .build();

        Set<ConstraintViolation<User>> validates = validator.validate(user);
        assertEquals(2, validates.size(), "Не верное количество пользователей");
    }

    @Test
    void userEmailWithOutCommercialAt() {
        User user = User.builder()
                .id(1)
                .email("mail.yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2000,01,01))
                .build();

        Set<ConstraintViolation<User>> validates = validator.validate(user);
        assertEquals(1, validates.size(), "Не верное количество пользователей");
    }

    @Test
    void userLoginIsBlink() {
        User user = User.builder()
                .id(1)
                .email("mail@yandex.ru")
                .login("")
                .name("name")
                .birthday(LocalDate.of(2000,01,01))
                .build();

        Set<ConstraintViolation<User>> validates = validator.validate(user);
        assertEquals(1, validates.size(), "Не верное количество пользователей");
    }

    @Test
    void userLoginIsEmpty() {
        User user = User.builder()
                .id(1)
                .email("mail@yandex.ru")
                .login(" ")
                .name("name")
                .birthday(LocalDate.of(2000,01,01))
                .build();

        Set<ConstraintViolation<User>> validates = validator.validate(user);
        assertEquals(1, validates.size(), "Не верное количество пользователей");
    }

    @Test
    void userNameIsBlink() {
        User user = User.builder()
                .id(1)
                .email("mail@yandex.ru")
                .login("login")
                .name("")
                .birthday(LocalDate.of(2000,01,01))
                .build();

        Set<ConstraintViolation<User>> validates = validator.validate(user);
        assertEquals(0, validates.size(), "Не верное количество пользователей");
    }
    @Test
    void userBirthdayAfterNow() {
        User user = User.builder()
                .id(1)
                .email("mail@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2100,01,01))
                .build();

        Set<ConstraintViolation<User>> validates = validator.validate(user);
        assertEquals(1, validates.size(), "Не верное количество пользователей");
    }

}
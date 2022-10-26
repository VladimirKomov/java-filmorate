package ru.yandex.practicum.filmorate.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.Constants.DATE_FIRST_FILM;

public class AfterFirstFilmValidator implements ConstraintValidator<AfterFirstFilm, LocalDate> {

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value.isAfter(DATE_FIRST_FILM);
    }
}
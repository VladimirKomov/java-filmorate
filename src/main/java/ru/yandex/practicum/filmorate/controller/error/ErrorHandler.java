package ru.yandex.practicum.filmorate.controller.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;

import javax.servlet.ServletException;
import javax.validation.ValidationException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLNonTransientException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundException(final DataNotFoundException e) {
        log.info(HttpStatus.NOT_FOUND + " {}", e.getMessage());
        return new ErrorResponse(HttpStatus.NOT_FOUND.toString(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validateException(final ValidationException e) {
        log.info(HttpStatus.BAD_REQUEST + " {}", e.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validateException(TypeMismatchException e) {
        log.info(HttpStatus.BAD_REQUEST + " {}", e.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validateException(MethodArgumentNotValidException e) {
        log.info(HttpStatus.BAD_REQUEST + " {}", e.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse methodNotSupported (final ServletException e) {
        log.info(HttpStatus.METHOD_NOT_ALLOWED + " {}", e.getMessage());
        return new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.toString(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse test (final DuplicateKeyException e) {
        log.info(HttpStatus.CONFLICT + " {}", e.getMessage());
        return new ErrorResponse(HttpStatus.CONFLICT.toString(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse throwable(final Throwable e) {
        log.info(HttpStatus.INTERNAL_SERVER_ERROR + " {}", e.getMessage());
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage());
    }

}

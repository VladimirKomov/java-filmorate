package ru.yandex.practicum.filmorate.controller.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;

import javax.validation.ValidationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundException(final DataNotFoundException e) {
        log.info(HttpStatus.NOT_FOUND + " {}", e.getMessage());
        return new ErrorResponse(HttpStatus.NOT_FOUND + " {}", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validateException(final ValidationException e) {
        log.info(HttpStatus.BAD_REQUEST + " {}", e.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST + " {}", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse throwable(final Throwable e) {
        log.info(HttpStatus.INTERNAL_SERVER_ERROR + " {}", e.getMessage());
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR + " {}", e.getMessage());
    }

}

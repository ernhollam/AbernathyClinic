package com.abernathyclinic.history.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Exception thrown when a resource is not found.
 */
@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(NoteNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String noteNotFoundException(NoteNotFoundException notFoundException) {
        log.error("Note was not found.", notFoundException);
        return "Note was not found:\n" + notFoundException.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String returnMessage(Exception exception) {
        log.error("An error occurred.", exception);
        return "An error occurred:\n " + exception.getMessage();
    }
}

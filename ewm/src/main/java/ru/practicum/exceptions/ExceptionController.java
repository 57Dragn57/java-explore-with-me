package ru.practicum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleIncorrectValidationException(ValidationException e) {
        return new ApiError(e.getMessage(), "The required object was not found.", HttpStatus.BAD_REQUEST, List.of());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleForbiddenException(ForbiddenException e) {
        return new ApiError(e.getMessage(), "The required object was forbidden.", HttpStatus.FORBIDDEN, List.of());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(NotFoundException e) {
        return new ApiError(e.getMessage(), "The required object was not found.", HttpStatus.NOT_FOUND, List.of());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleIncorrectValidationException(ConflictException e) {
        return new ApiError(e.getMessage(), "The required object has conflict.", HttpStatus.CONFLICT, List.of());
    }

    @ExceptionHandler(ConflictValueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleIncorrectConflictValueException(ConflictValueException e) {
        return new ApiError(e.getMessage(), "The required object was not found.", HttpStatus.BAD_REQUEST, List.of());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleThrowable(Throwable e) {
        return new ApiError("An unexpected error has occurred.", "The required object was not found.", HttpStatus.BAD_REQUEST, List.of());
    }
}
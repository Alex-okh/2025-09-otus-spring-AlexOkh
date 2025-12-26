package ru.otus.hw.hw11webflux.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.otus.hw.hw11webflux.dto.ErrorDto;
import ru.otus.hw.hw11webflux.exceptions.EntityNotFoundException;
import java.time.LocalDateTime;

@RestControllerAdvice(assignableTypes = {AuthorController.class, CommentController.class, BookController.class,
                                         GenreController.class})
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> handleEntityNotFoundException(EntityNotFoundException e, WebRequest request) {
        log.error(e.getMessage());
        var error = new ErrorDto(LocalDateTime.now(), HttpStatus.NOT_FOUND, e.getMessage(),
                                 request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(error);
    }
}

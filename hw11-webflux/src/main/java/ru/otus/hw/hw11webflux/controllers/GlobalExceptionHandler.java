package ru.otus.hw.hw11webflux.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import ru.otus.hw.hw11webflux.dto.ErrorDto;
import ru.otus.hw.hw11webflux.exceptions.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice(assignableTypes = {AuthorController.class, CommentController.class, BookController.class,
                                         GenreController.class})
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationException(MethodArgumentNotValidException e,
                                                              ServerWebExchange request) {
        log.warn(e.getMessage());
        List<String> fieldErrors = new ArrayList<>();
        for (FieldError fieldError : e.getBindingResult()
                                      .getFieldErrors()) {
            fieldErrors.add(fieldError.getDefaultMessage());
        }

        var error = new ErrorDto(LocalDateTime.now(),
                                 HttpStatus.BAD_REQUEST,
                                 fieldErrors.stream().collect(Collectors.joining(",")),
                                 request.getRequest()
                                        .getPath()
                                        .toString()
                                        .replace("uri=",""));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> handleEntityNotFoundException(EntityNotFoundException e,
                                                                  ServerWebExchange request) {
        log.error(e.getMessage());
        var error = new ErrorDto(LocalDateTime.now(),
                                 HttpStatus.NOT_FOUND,
                                 e.getMessage(),
                                 request.getRequest()
                                        .getPath()
                                        .toString()
                                        .replace("uri=",""));
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(error);
    }
}

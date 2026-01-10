package ru.otus.hw.hw11webflux.dto;

import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

public record ErrorDto(LocalDateTime timestamp, HttpStatus status, String errorMessage, String path) {
}

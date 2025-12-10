package ru.otus.hw.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record BookDto(
        @Min(value = 0, message = "Book id can not be negative")
        long id,

        @NotBlank(message = "Название книги не может быть пустым")
        String title,

        String authorName,

        @NotNull(message = "Выберите автора")
        Long authorId,

        String genres,

        @NotEmpty(message = "Выбберите хотя бы один жанр")
        Set<Long> genreIds) {
}
package ru.otus.hw.hw11webflux.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record NewBookDto(@NotBlank(message = "Название книги не может быть пустым") String title,
                         @NotBlank(message = "Выберите автора") String authorId,
                         @NotEmpty(message = "Выберите хотя бы один жанр") Set<@NotBlank (
                                 message = "Код жанра не может быть пустым") String> genres) {
}



package ru.otus.hw.hw11webflux.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record NewBookDto(@NotBlank(message = "Название книги не может быть пустым") String title,
                         @NotNull(message = "Выберите автора") String authorId,
                         @NotEmpty(message = "Выберите хотя бы один жанр") Set<@NotBlank String> genres) {
}



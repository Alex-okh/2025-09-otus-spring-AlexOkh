package ru.otus.hw.dto;

import jakarta.validation.constraints.NotNull;

public record NewCommentDto(@NotNull(message = "Book id should not be null") Long bookId,

                            String text) {
}

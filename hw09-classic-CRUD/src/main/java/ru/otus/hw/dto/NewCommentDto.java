package ru.otus.hw.dto;

import jakarta.validation.constraints.Min;

public record NewCommentDto(@Min(value = 1, message = "Book id should be positive") Long bookId,

                            String text) {
}

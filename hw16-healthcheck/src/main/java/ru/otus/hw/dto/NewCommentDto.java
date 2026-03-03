package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;

public record NewCommentDto(Long bookId,

                            @NotBlank(message = "Comment text should not be blank") String text) {
}

package ru.otus.hw.hw11webflux.dto;

import jakarta.validation.constraints.NotBlank;

public record NewCommentDto(@NotBlank(message = "Comment text should not be blank") String text) {
}

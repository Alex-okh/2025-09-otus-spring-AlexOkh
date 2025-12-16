package ru.otus.hw.dto;

import java.util.Set;

public record BookDto(Long id, String title, String authorName, Long authorId, String genres, Set<Long> genreIds) {}
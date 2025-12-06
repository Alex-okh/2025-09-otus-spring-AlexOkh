package ru.otus.hw.dto;

import java.util.Set;

public record UpdateBookDTO(Long id, String title, Integer authorId, Set<Long> genreIds) {}

package ru.otus.hw.dto;

import ru.otus.hw.models.Author;
import java.util.Set;

public record BookDTO(long id, String title, Author author, String genres, Set<Long> genreIds) {
}
package ru.otus.hw.hw11webflux.dto;

import java.util.Set;

public record BookDto(String id, String title, AuthorDto author, Set<GenreDto> genres) {
}
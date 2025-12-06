package ru.otus.hw.dto;

import ru.otus.hw.models.Author;

public record BookDTO(long id, String title, Author author, String genres) {}
package ru.otus.hw.hw11webflux.service;

import reactor.core.publisher.Flux;
import ru.otus.hw.hw11webflux.dto.GenreDto;

public interface GenreService {
    Flux<GenreDto> findAll();
}

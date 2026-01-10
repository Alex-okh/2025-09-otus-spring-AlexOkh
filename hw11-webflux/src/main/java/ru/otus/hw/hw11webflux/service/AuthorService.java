package ru.otus.hw.hw11webflux.service;

import reactor.core.publisher.Flux;
import ru.otus.hw.hw11webflux.dto.AuthorDto;

public interface AuthorService {
    Flux<AuthorDto> findAll();
}

package ru.otus.hw.hw11webflux.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.otus.hw.hw11webflux.dto.AuthorDto;
import ru.otus.hw.hw11webflux.dto.GenreDto;
import ru.otus.hw.hw11webflux.mappers.AuthorMapper;
import ru.otus.hw.hw11webflux.mappers.GenreMapper;
import ru.otus.hw.hw11webflux.repositories.AuthorRepository;
import ru.otus.hw.hw11webflux.repositories.GenreRepository;
import java.time.Duration;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    @Override
    public Flux<GenreDto> findAll() {
        return genreRepository.findAll().map(genreMapper::toDto);
    }
}

package ru.otus.hw.hw11webflux.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.otus.hw.hw11webflux.dto.AuthorDto;
import ru.otus.hw.hw11webflux.mappers.AuthorMapper;
import ru.otus.hw.hw11webflux.repositories.AuthorRepository;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    @Override
    public Flux<AuthorDto> findAll() {
        return authorRepository.findAll().map(authorMapper::toDto);
    }
}

package ru.otus.hw.hw11webflux.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.hw11webflux.dto.AuthorDto;
import ru.otus.hw.hw11webflux.service.AuthorService;

@RestController
@AllArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("api/flux/authors")
    public Flux<AuthorDto> getAllAuthors() {
        return authorService.findAll();
    }
}

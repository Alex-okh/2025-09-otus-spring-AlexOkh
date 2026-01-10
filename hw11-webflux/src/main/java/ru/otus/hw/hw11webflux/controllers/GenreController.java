package ru.otus.hw.hw11webflux.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.hw11webflux.dto.GenreDto;
import ru.otus.hw.hw11webflux.service.GenreService;

@RestController
@AllArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("api/flux/genres")
    public Flux<GenreDto> getAllAuthors() {
        return genreService.findAll();
    }
}

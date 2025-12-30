package ru.otus.hw.hw11webflux.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
@AllArgsConstructor
public class ViewController {

    @GetMapping("/authors")
    public Mono<String> showAuthors() {
        return Mono.just("authors");
    }

    @GetMapping("/genres")
    public Mono<String> showGenres() {
        return Mono.just("genres");
    }

    @GetMapping("/books")
    public Mono<String> showBooks() {
        return Mono.just("books");
    }

    @GetMapping("/books/create")
    public Mono<String> createBook() {
        return Mono.just("editbook");
    }

    @GetMapping("/books/{id}/edit")
    public Mono<String> editBook() {
        return Mono.just("editbook");
    }

    @GetMapping("/books/{id}")
    public Mono<String> showBook() {
        return Mono.just("book");
    }
}



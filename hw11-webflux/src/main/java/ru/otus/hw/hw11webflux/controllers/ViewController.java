package ru.otus.hw.hw11webflux.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class ViewController {

    @GetMapping("/authors")
    public String showAuthors() {
        return "authors";
    }

    @GetMapping("/genres")
    public String showGenres() {
        return "genres";
    }

    @GetMapping("/books")
    public String showBooks() {
        return "books";
    }

    @GetMapping("/books/create")
    public String createBook() {
        return "editbook";
    }

    @GetMapping("/books/{id}/edit")
    public String editBook() {
        return "editbook";
    }

    @GetMapping("/books/{id}")
    public String showBook() {
        return "book";
    }
}



package ru.otus.hw.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class BookController {

    @GetMapping("/books")
    public String showAllBooks() {
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

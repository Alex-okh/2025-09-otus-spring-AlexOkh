package ru.otus.hw.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class AuthorController {

    @GetMapping("/authors")
    public String getAuthors() {
        return "authors";
    }
}

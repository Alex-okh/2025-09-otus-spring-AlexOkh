package ru.otus.hw.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.services.GenreService;

@Controller
@AllArgsConstructor
public class GenresController {
    private final GenreService genreService;

    @GetMapping("/genres")
    public String showGenres() {
        return "genres";
    }
}

package ru.otus.hw.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.services.AuthorService;

@Controller
@AllArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    private final AuthorMapper authorMapper;

    @GetMapping("/authors")
    public String showAuthors(Model model) {
        var authors = authorMapper.authorToDto(authorService.findAll());
        model.addAttribute("authors", authors);
        return "authors";
    }
}

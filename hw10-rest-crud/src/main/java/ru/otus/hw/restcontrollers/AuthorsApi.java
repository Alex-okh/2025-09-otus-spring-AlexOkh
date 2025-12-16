package ru.otus.hw.restcontrollers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;
import java.util.List;

@RestController
@AllArgsConstructor
public class AuthorsApi {

    private final AuthorService authorService;

    @GetMapping("api/authors")
    public List<AuthorDto> getAuthors() {
        return authorService.findAll();
    }
}

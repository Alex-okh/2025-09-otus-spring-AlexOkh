package ru.otus.hw.restcontrollers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;
import java.util.List;

@RestController
@AllArgsConstructor
public class GenresApi {

    private final GenreService genreService;

    @GetMapping("api/genres")
    public List<GenreDto> getAllGenres() {
        return genreService.findAll();
    }
}

package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;
import java.util.ArrayList;
import java.util.List;

@Component
public class GenreMapper {

    public GenreDto genreToDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }

    public List<GenreDto> genreToDto(List<Genre> genres) {
        var genreDtos = new ArrayList<GenreDto>();
        for (var genre : genres) {
            genreDtos.add(genreToDto(genre));
        }
        return genreDtos;
    }
}
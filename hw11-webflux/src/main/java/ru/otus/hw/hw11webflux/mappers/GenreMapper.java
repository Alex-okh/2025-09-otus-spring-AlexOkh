package ru.otus.hw.hw11webflux.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.hw11webflux.dto.GenreDto;
import ru.otus.hw.hw11webflux.models.Genre;
import java.util.ArrayList;
import java.util.List;

@Component
public class GenreMapper {

    public GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }

    public List<GenreDto> toDto(List<Genre> genres) {
        var genreDtos = new ArrayList<GenreDto>();
        for (var genre : genres) {
            genreDtos.add(toDto(genre));
        }
        return genreDtos;
    }
}
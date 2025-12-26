package ru.otus.hw.hw11webflux.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.hw11webflux.dto.GenreDto;
import ru.otus.hw.hw11webflux.models.Genre;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GenreMapper {

    public GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }

    public Set<GenreDto> toDto(Set<Genre> genres) {
        var genreDtos = new HashSet<GenreDto>();
        for (var genre : genres) {
            genreDtos.add(toDto(genre));
        }
        return genreDtos;
    }
}
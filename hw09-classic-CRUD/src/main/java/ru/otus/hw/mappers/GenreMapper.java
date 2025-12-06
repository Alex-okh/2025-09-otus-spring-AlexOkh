package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.models.Genre;
import java.util.ArrayList;
import java.util.List;

@Component
public class GenreMapper {

    public GenreDTO genreToDto(Genre genre) {
        return new GenreDTO(genre.getId(), genre.getName());
    }

    public List<GenreDTO> genreToDto(List<Genre> genres) {
        var genreDTOs = new ArrayList<GenreDTO>();
        for (var genre : genres) {
            genreDTOs.add(genreToDto(genre));
        }
        return genreDTOs;
    }
}
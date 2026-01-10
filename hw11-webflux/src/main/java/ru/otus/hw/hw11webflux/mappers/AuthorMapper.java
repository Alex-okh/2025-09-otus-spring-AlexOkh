package ru.otus.hw.hw11webflux.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.hw11webflux.dto.AuthorDto;
import ru.otus.hw.hw11webflux.models.Author;
import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorMapper {
    public AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }

    public List<AuthorDto> toDto(List<Author> authors) {
        var authorDtos = new ArrayList<AuthorDto>();
        for (var author : authors) {
            authorDtos.add(toDto(author));
        }
        return authorDtos;
    }
}


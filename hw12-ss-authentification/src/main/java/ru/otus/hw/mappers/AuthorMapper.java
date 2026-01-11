package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;
import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorMapper {
    public AuthorDto authorToDto(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }

    public List<AuthorDto> authorToDto(List<Author> authors) {
        var authorDtos = new ArrayList<AuthorDto>();
        for (var author : authors) {
            authorDtos.add(authorToDto(author));
        }
        return authorDtos;
    }
}


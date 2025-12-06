package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.models.Author;
import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorMapper {
    public AuthorDTO authorToDto(Author author) {
        return new AuthorDTO(author.getId(), author.getFullName());
    }

    public List<AuthorDTO> authorToDto(List<Author> authors) {
        var authorDTOs = new ArrayList<AuthorDTO>();
        for (var author : authors) {
            authorDTOs.add(authorToDto(author));
        }
        return authorDTOs;
    }
}


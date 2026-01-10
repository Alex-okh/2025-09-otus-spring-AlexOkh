package ru.otus.hw.hw11webflux.mappers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.hw11webflux.dto.BookDto;
import ru.otus.hw.hw11webflux.models.Book;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class BookMapper {

    private final GenreMapper genreMapper;

    private final AuthorMapper authorMapper;

    public BookDto toDto(Book book) {
        var genreDtos = genreMapper.toDto(book.getGenres());
        var authorDto = authorMapper.toDto(book.getAuthor());
        return new BookDto(book.getId(), book.getTitle(), authorDto, genreDtos);
    }

    public List<BookDto> toDto(List<Book> books) {
        var bookDtos = new ArrayList<BookDto>();
        for (var book : books) {
            bookDtos.add(toDto(book));
        }
        return bookDtos;
    }

}

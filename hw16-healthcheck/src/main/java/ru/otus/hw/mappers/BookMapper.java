package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    public BookDto bookToDto(Book book) {
        String genres = book.getGenres()
                            .stream()
                            .map(Genre::getName)
                            .collect(Collectors.joining(", "));
        Set<Long> genreIds = book.getGenres()
                                 .stream()
                                 .mapToLong(Genre::getId)
                                 .boxed()
                                 .collect(Collectors.toSet());

        return new BookDto(book.getId(), book.getTitle(), book.getAuthor()
                                                              .getFullName(), book.getAuthor()
                                                                                  .getId(), genres, genreIds);
    }

    public List<BookDto> bookToDto(List<Book> books) {
        var bookDtos = new ArrayList<BookDto>();
        for (var book : books) {
            bookDtos.add(bookToDto(book));
        }
        return bookDtos;
    }

}

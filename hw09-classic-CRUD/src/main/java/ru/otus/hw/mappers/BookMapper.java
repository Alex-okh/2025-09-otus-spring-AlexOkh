package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    public BookDTO bookToDto(Book book) {
        String genres = book.getGenres()
                            .stream()
                            .map(Genre::getName)
                            .collect(Collectors.joining(", "));
        return new BookDTO(book.getId(), book.getTitle(), book.getAuthor(), genres);
    }

    public List<BookDTO> bookToDto(List<Book> books) {
        var bookDtos = new ArrayList<BookDTO>();
        for (var book : books) {
            bookDtos.add(bookToDto(book));
        }
        return bookDtos;
    }

}

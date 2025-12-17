package ru.otus.hw.restcontrollers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.NewBookDto;
import ru.otus.hw.dto.UpdateBookDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentsService;
import ru.otus.hw.services.GenreService;
import java.util.List;

@RestController
@AllArgsConstructor
public class BooksApi {
    private final BookService bookService;

    private final CommentsService commentsService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/api/books")
    public List<BookDto> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/api/books/{id}")
    public BookDto getBook(@PathVariable long id) {
        return bookService.findById(id);
    }

    @DeleteMapping("api/books/{id}")
    public void deleteBook(@PathVariable int id) {
        bookService.deleteById(id);
    }

    @PutMapping("api/books")
    public void updateBook(@RequestBody UpdateBookDto book) {
       bookService.update(book);
    }

    @PostMapping("api/books")
    public void createBook(@RequestBody NewBookDto book) {
        bookService.create(book);
    }
}

package ru.otus.hw.rest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.NewBookDto;
import ru.otus.hw.dto.UpdateBookDto;
import ru.otus.hw.services.BookService;
import java.util.List;

@RestController
@AllArgsConstructor
public class BooksRestController {
    private final BookService bookService;

    @GetMapping("/api/books")
    public List<BookDto> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/api/books/{id}")
    public BookDto getBook(@PathVariable long id) {
        return bookService.findById(id);
    }

    @DeleteMapping("api/books/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable int id) {
        bookService.deleteById(id);
    }

    @PutMapping("api/books")
    public ResponseEntity<BookDto> updateBook(@RequestBody @Valid UpdateBookDto book) {
        var updatedBook = bookService.update(book);
        return ResponseEntity.ok()
                             .body(updatedBook);
    }

    @PostMapping("api/books")
    public ResponseEntity<BookDto> createBook(@RequestBody @Valid NewBookDto book) {
        var createdBook = bookService.create(book);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(createdBook);
    }
}

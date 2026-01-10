package ru.otus.hw.hw11webflux.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.hw11webflux.dto.BookDto;
import ru.otus.hw.hw11webflux.dto.NewBookDto;
import ru.otus.hw.hw11webflux.service.BookService;

@RestController
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("api/flux/books")
    public Flux<BookDto> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/api/flux/books/{id}")
    public Mono<BookDto> getBook(@PathVariable String id) {
        return bookService.getBook(id);
    }

    @DeleteMapping("api/flux/books/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBook(@PathVariable String id) {
        return bookService.deleteBook(id);
    }

    @PostMapping("api/flux/books")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BookDto> createBook(@RequestBody @Valid NewBookDto newbook) {
        return bookService.addBook(newbook);
    }

    @PutMapping("api/flux/books/{bookId}")
    public Mono<BookDto> updateBook(@RequestBody @Valid NewBookDto newbook, @PathVariable String bookId) {
        return bookService.updateBook(bookId, newbook);
    }
}

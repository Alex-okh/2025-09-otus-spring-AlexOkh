package ru.otus.hw.hw11webflux.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.hw11webflux.dto.BookDto;

public interface BookService {

    Mono<BookDto> getBook(String bookId);

    Flux<BookDto> getAllBooks();

    Mono<BookDto> addBook(BookDto bookDto);

    Mono<BookDto> updateBook(BookDto bookDto);

    Mono<Void> deleteBook(String bookId);
}

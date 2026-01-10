package ru.otus.hw.hw11webflux.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.hw11webflux.dto.BookDto;
import ru.otus.hw.hw11webflux.dto.NewBookDto;

public interface BookService {

    Mono<BookDto> getBook(String bookId);

    Flux<BookDto> getAllBooks();

    Mono<BookDto> addBook(NewBookDto bookDto);

    Mono<BookDto> updateBook(String bookId, NewBookDto bookDto);

    Mono<Void> deleteBook(String bookId);
}

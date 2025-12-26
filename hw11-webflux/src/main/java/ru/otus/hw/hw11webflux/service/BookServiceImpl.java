package ru.otus.hw.hw11webflux.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.hw11webflux.dto.BookDto;
import ru.otus.hw.hw11webflux.exceptions.EntityNotFoundException;
import ru.otus.hw.hw11webflux.mappers.BookMapper;
import ru.otus.hw.hw11webflux.repositories.BookRepository;
import ru.otus.hw.hw11webflux.repositories.CommentRepository;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final CommentRepository commentRepository;

    @Override
    public Mono<BookDto> getBook(String bookId) {
        return bookRepository.findById(bookId)
                             .switchIfEmpty(Mono.error(
                                     new EntityNotFoundException("Book with Id %s not found".formatted(bookId))))
                             .map(bookMapper::toDto);
    }

    @Override
    public Flux<BookDto> getAllBooks() {
        return bookRepository.findAll()
                             .map(bookMapper::toDto);
    }

    @Override
    public Mono<BookDto> addBook(BookDto bookDto) {
        return null;
    }

    @Override
    public Mono<BookDto> updateBook(BookDto bookDto) {
        return null;
    }

    @Override
    public Mono<Void> deleteBook(String bookId) {
        return commentRepository.deleteByBookId(bookId).then(bookRepository.deleteById(bookId));
    }
}

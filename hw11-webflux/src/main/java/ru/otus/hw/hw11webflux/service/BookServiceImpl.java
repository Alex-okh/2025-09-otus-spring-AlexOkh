package ru.otus.hw.hw11webflux.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.hw11webflux.dto.BookDto;
import ru.otus.hw.hw11webflux.dto.NewBookDto;
import ru.otus.hw.hw11webflux.exceptions.EntityNotFoundException;
import ru.otus.hw.hw11webflux.mappers.BookMapper;
import ru.otus.hw.hw11webflux.models.Book;
import ru.otus.hw.hw11webflux.repositories.AuthorRepository;
import ru.otus.hw.hw11webflux.repositories.BookRepository;
import ru.otus.hw.hw11webflux.repositories.CommentRepository;
import ru.otus.hw.hw11webflux.repositories.GenreRepository;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

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
    public Mono<BookDto> addBook(NewBookDto bookDto) {
        var genreIds = bookDto.genres();
        if (genreIds == null || genreIds.isEmpty()) {
            return Mono.error(new EntityNotFoundException("Genres cannot be empty"));
        }
        var author = authorRepository.findById(bookDto.authorId())
                                     .switchIfEmpty(Mono.error(new EntityNotFoundException(
                                             "Author with Id %s not found".formatted(bookDto.authorId()))));
        var genres = Flux.fromIterable(genreIds)
                         .flatMap(genreRepository::findById)
                         .collect(Collectors.toSet())
                         .flatMap(g -> {
                             if (g == null || g.isEmpty() || g.size() != genreIds.size()) {
                                 return Mono.error(new EntityNotFoundException("One or more genres Id not found"));
                             }
                             return Mono.just(g);
                         });
        return Mono.zip(author, genres)
                   .flatMap(tuple -> {
                       var book = new Book(null, bookDto.title(), tuple.getT1(), tuple.getT2());
                       return bookRepository.save(book);
                   })
                   .map(bookMapper::toDto);
    }

    @Override
    public Mono<BookDto> updateBook(String bookId, NewBookDto bookDto) {
        var author = authorRepository.findById(bookDto.authorId())
                                     .switchIfEmpty(Mono.error(new EntityNotFoundException(
                                             "Author with Id %s not found".formatted(bookDto.authorId()))));
        var genres = Flux.fromIterable(bookDto.genres())
                         .flatMap(genreRepository::findById)
                         .collect(Collectors.toSet())
                         .flatMap(g -> {if (g.isEmpty() || g.size() != bookDto.genres()
                                                                   .size()) {
                                 return Mono.error(new EntityNotFoundException("One or more genres Id not found"));
                             }
                             return Mono.just(g);
                         });
        return bookRepository.findById(bookId)
                             .switchIfEmpty(Mono.error(
                                     new EntityNotFoundException("Book with Id %s not found".formatted(bookId))))
                             .flatMap(book -> Mono.zip(author, genres)
                                                  .flatMap(tuple -> {
                                                      book.setTitle(bookDto.title());
                                                      book.setAuthor(tuple.getT1());
                                                      book.setGenres(tuple.getT2());
                                                      return bookRepository.save(book);
                                                  }))
                             .map(bookMapper::toDto);
    }

    @Override
    public Mono<Void> deleteBook(String bookId) {
        return commentRepository.deleteByBookId(bookId)
                                .then(bookRepository.deleteById(bookId));
    }
}

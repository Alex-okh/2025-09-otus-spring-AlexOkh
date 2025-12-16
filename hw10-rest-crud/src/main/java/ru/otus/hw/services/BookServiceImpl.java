package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.NewBookDto;
import ru.otus.hw.dto.UpdateBookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import java.util.List;
import java.util.Set;
import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Override
    @Transactional(readOnly = true)
    public BookDto findById(long id) {
        return bookMapper.bookToDto(bookRepository.findById(id)
                                                  .orElseThrow(() -> new EntityNotFoundException(
                                                          "Book with id %d not found".formatted(id))));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        var books = bookRepository.findAll();
        return bookMapper.bookToDto(books);
    }

    @Override
    @Transactional
    public BookDto create(NewBookDto bookDto) {
        var author = getAuthor(bookDto.authorId());
        var genres = getGenres(bookDto.genreIds());
        var book = new Book(0, bookDto.title(), author, genres);
        return bookMapper.bookToDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public BookDto update(UpdateBookDto bookDto) {
        var author = getAuthor(bookDto.authorId());
        var genres = getGenres(bookDto.genreIds());
        var book = bookRepository.findById(bookDto.id())
                                 .orElseThrow(() -> new EntityNotFoundException(
                                         "Book with id %d not found".formatted(bookDto.id())));
        book.setTitle(bookDto.title());
        book.setAuthor(author);
        book.setGenres(genres);
        return bookMapper.bookToDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private List<Genre> getGenres(Set<Long> genresIds) {
        var genres = genreRepository.findAllById(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }
        return genres;
    }

    private Author getAuthor(long authorId) {
        return authorRepository.findById(authorId)
                               .orElseThrow(() -> new EntityNotFoundException(
                                       "Author with id %d not found".formatted(authorId)));

    }
}

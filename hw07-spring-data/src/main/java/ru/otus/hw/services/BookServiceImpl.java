package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        var books = bookRepository.findAll();
        books.forEach(book -> book.getGenres()
                                  .size());
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public Book create(String title, long authorId, Set<Long> genresIds) {
        var author = getAuthor(authorId);
        var genres = getGenres(genresIds);
        var book = new Book(0, title, author, genres);
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book update(long id, String title, long authorId, Set<Long> genresIds) {
        var author = getAuthor(authorId);
        var genres = getGenres(genresIds);
        var book = bookRepository.findById(id)
                                 .orElseThrow(
                                         () -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenres(genres);
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private List<Genre> getGenres(Set<Long> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }
        var genres = genreRepository.findAllByIdIn(genresIds);
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

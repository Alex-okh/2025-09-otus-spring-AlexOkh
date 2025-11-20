package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaGenreRepository;
import java.util.List;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("Сервис книг должен")
@DataJpaTest
@Import({BookServiceImpl.class, AuthorServiceImpl.class, GenreServiceImpl.class, JpaGenreRepository.class, JpaAuthorRepository.class, JpaBookRepository.class})
@Transactional(propagation = Propagation.NEVER)
class BookServiceImplTest {
    public static final String UPDATED_BOOK_TITLE = "BookTitle_10500";
    public static final long UPDATED_AUTHOR_ID = 2L;
    public static final Set<Long> UPDATED_GENRES_IDS = Set.of(1L);
    static final int EXPECTED_BOOKS_COUNT = 3;
    @Autowired
    private BookService bookService;

    @DisplayName("Загружать книгу по ID")
    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3})
    void findByIdTest(long id) {
        var book = bookService.findById(id);
        assertThat(book).isNotEmpty()
                        .get()
                        .hasFieldOrPropertyWithValue("title", "BookTitle_" + id);
        assertThatCode(() -> {
            book.get()
                .getAuthor()
                .getFullName();
            book.get()
                .getGenres()
                .size();
        }).doesNotThrowAnyException();
    }

    @DisplayName("Загружать все книги")
    @Test
    void findAllTest() {
        var books = bookService.findAll();
        assertThat(books).isNotEmpty()
                         .hasSize(EXPECTED_BOOKS_COUNT);

        books.forEach(book -> {
            assertThatCode(() -> {
                book.getAuthor()
                    .getFullName();
                book.getGenres()
                    .size();
            }).doesNotThrowAnyException();
        });
    }

    @DirtiesContext
    @DisplayName("Вставлять новую книгу")
    @Test
    void insert() {
        var expectedBook = new Book(0, UPDATED_BOOK_TITLE, new Author(3, "Author_3"), List.of(new Genre(1, "Genre_1")));
        var returnedBook = bookService.insert(UPDATED_BOOK_TITLE, 3L, UPDATED_GENRES_IDS);
        var foundBook = bookService.findById(returnedBook.getId());
        assertThat(foundBook).isNotEmpty();
        var actualBook = foundBook.get();
        assertThat(returnedBook).isNotNull()
                                .matches(book -> book.getId() > 0)
                                .usingRecursiveComparison()
                                .ignoringFields("id")
                                .ignoringExpectedNullFields()
                                .isEqualTo(expectedBook);

        assertThat(actualBook).isNotNull()
                              .matches(book -> book.getId() > 0)
                              .usingRecursiveComparison()
                              .ignoringFields("id")
                              .ignoringExpectedNullFields()
                              .isEqualTo(expectedBook);
    }

    @DirtiesContext
    @DisplayName("Обновлять книгу без изменения ID")
    @Test
    void update() {
        var expectedBook = new Book(3L, UPDATED_BOOK_TITLE, new Author(UPDATED_AUTHOR_ID, "Author_2"),
                                    List.of(new Genre(1, "Genre_1")));
        var returnedBook = bookService.update(3L, UPDATED_BOOK_TITLE, UPDATED_AUTHOR_ID, UPDATED_GENRES_IDS);
        var actualBook = bookService.findById(returnedBook.getId())
                                    .orElseGet(null);
        assertThat(returnedBook).isNotNull()
                                .matches(book -> book.getId() > 0)
                                .usingRecursiveComparison()
                                .ignoringExpectedNullFields()
                                .isEqualTo(expectedBook);

        assertThat(actualBook).isNotNull()
                              .matches(book -> book.getId() > 0)
                              .usingRecursiveComparison()
                              .ignoringExpectedNullFields()
                              .isEqualTo(expectedBook);
    }

    @DirtiesContext
    @DisplayName("Удалять книгу по id")
    @Test
    void deleteById() {
        assertThat(bookService.findById(1L)).isNotEmpty();
        bookService.deleteById(1L);
        assertThat(bookService.findById(1L)).isEmpty();
    }
}
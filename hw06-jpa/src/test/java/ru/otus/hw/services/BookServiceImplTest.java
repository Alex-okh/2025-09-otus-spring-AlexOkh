package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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
import ru.otus.hw.repositories.JpaCommentRepository;
import ru.otus.hw.repositories.JpaGenreRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис книг должен")
@DataJpaTest
@Import({CommentsServiceImpl.class, BookServiceImpl.class, AuthorServiceImpl.class, GenreServiceImpl.class, JpaGenreRepository.class, JpaAuthorRepository.class, JpaBookRepository.class, JpaCommentRepository.class})
@Transactional(propagation = Propagation.NEVER)
class BookServiceImplTest {
    public static final String UPDATED_BOOK_TITLE = "BookTitle_10500";
    public static final long UPDATED_AUTHOR_ID = 2L;
    public static final Set<Long> UPDATED_GENRES_IDS = Set.of(1L);
    static final int EXPECTED_BOOKS_COUNT = 3;

    @Autowired
    private BookService bookService;

    @Autowired
    private CommentsService commentsService;

    private List<Book> testBooks;

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4)
                        .boxed()
                        .map(id -> new Author(id, "Author_" + id))
                        .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7)
                        .boxed()
                        .map(id -> new Genre(id, "Genre_" + id))
                        .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4)
                        .boxed()
                        .map(id -> new Book(id, "BookTitle_" + id, dbAuthors.get(id - 1),
                                            dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)))
                        .toList();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

    @BeforeEach
    void Setup() {
        testBooks = getDbBooks(getDbAuthors(), getDbGenres());
         }

    @DisplayName("Загружать книгу по ID")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void findByIdTest(Book expectedBook) {
        var actualBook = bookService.findById(expectedBook.getId());
        assertThat(actualBook).isPresent()
                              .get()
                              .usingRecursiveComparison()
                              .ignoringExpectedNullFields()
                              .isEqualTo(expectedBook);
    }

    @DisplayName("Загружать все книги")
    @Test
    void findAllTest() {
        var actualBooks = bookService.findAll();
        assertThat(actualBooks).isNotEmpty()
                               .hasSize(EXPECTED_BOOKS_COUNT)
                               .usingRecursiveComparison()
                               .ignoringExpectedNullFields()
                               .isEqualTo(testBooks);

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
    @DisplayName("Удалять комментарии к книге при ее удалении по id")
    @Test
    void deleteByIdCommentsTest() {
        assertThat(bookService.findById(1L)).isNotEmpty();
        assertThat(commentsService.findAllByBookId(1L)).isNotEmpty();
        bookService.deleteById(1L);
        assertThat(bookService.findById(1L)).isEmpty();
        assertThat(commentsService.findAllByBookId(1L)).isEmpty();
    }
}
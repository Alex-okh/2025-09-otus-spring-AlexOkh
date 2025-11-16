package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("репозиторий для работы с книгами должен:")
@DataJpaTest
@Import(JPABookRepository.class)
class JPABookRepositoryTest {

    private static final int EXPECTED_BOOK_COUNT = 3;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        assertThat(em.find(Book.class, 1L)).isNotNull();
        bookRepository.deleteById(1L);
        assertThat(em.find(Book.class, 1L)).isNull();
    }

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @CsvSource({"1, BookTitle_1, Author_1, Genre_1, 2, Comment1_1, 2", "2, BookTitle_2, Author_2, Genre_3, 2, '', 0"})
    void shouldReturnCorrectBookById(long id, String title, String author1, String genre1, int genreCount,
                                     String comment1, int commentCount) {
        var actualBook = bookRepository.findById(id);
        assertThat(actualBook).isPresent()
                              .get()
                              .hasFieldOrPropertyWithValue("title", title);

        assertThat(actualBook.get()
                             .getAuthor()
                             .getFullName()).isEqualTo(author1);
        assertThat(actualBook.get()
                             .getGenres()).extracting("name")
                                          .contains(genre1);
        assertThat(actualBook.get()
                             .getGenres()).hasSize(genreCount);
        assertThat(actualBook.get()
                             .getComments()).hasSize(commentCount);
        if (!comment1.isEmpty()) {
            assertThat(actualBook.get()
                                 .getComments()).extracting("text")
                                                .contains(comment1);
        } else {
            assertThat(actualBook.get()
                                 .getComments()).isEmpty();
        }

    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = bookRepository.findAll();
        assertThat(actualBooks).hasSize(EXPECTED_BOOK_COUNT);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var expectedBook = new Book(0, "BookTitle_10500", new Author(3, "Author_3"), List.of(new Genre(1, "Genre_1")),
                                    null);
        var returnedBook = bookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                                .matches(book -> book.getId() > 0)
                                .usingRecursiveComparison()
                                .ignoringExpectedNullFields()
                                .isEqualTo(expectedBook);

        assertThat(em.find(Book.class, expectedBook.getId())).isNotNull()
                                                             .isEqualTo(expectedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = new Book(1, "BookTitle_10500", new Author(3, "Author_3"), List.of(new Genre(1, "Genre_1")),
                                    null);

        var returnedBook = bookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                                .matches(book -> book.getId() > 0)
                                .usingRecursiveComparison()
                                .ignoringExpectedNullFields()
                                .isEqualTo(expectedBook);

        assertThat(em.find(Book.class, expectedBook.getId())).isNotNull()
                                                             .usingRecursiveComparison()
                                                             .ignoringExpectedNullFields()
                                                             .isEqualTo(expectedBook);
    }
}



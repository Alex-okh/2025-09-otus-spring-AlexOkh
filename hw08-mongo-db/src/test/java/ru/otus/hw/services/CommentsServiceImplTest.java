package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import java.util.List;
import java.util.stream.IntStream;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис комментариев должен")
@DataMongoTest
@Import({CommentsServiceImpl.class})
@Transactional(propagation = Propagation.NEVER)
class CommentsServiceImplTest {

    @Autowired
    private CommentsService commentsService;

    private List<Comment> testComments;

    private List<Book> testBooks;

    private static List<Comment> getDbComments(List<Book> books) {
        return IntStream.range(0, 8)
                        .boxed()
                        .map(id -> new Comment("Comment_" + ((id % 3) + 1) + "_" + (id + 1), books.get(id % 3)))
                        .toList();
    }
    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4)
                        .boxed()
                        .map(id -> new Author("a_id_" + id, "Author_" + id))
                        .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7)
                        .boxed()
                        .map(id -> new Genre("g_id_" + id, "Genre_" + id))
                        .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4)
                        .boxed()
                        .map(id -> new Book("b_id_" + id, "BookTitle_" + id, dbAuthors.get(id - 1),
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
        testBooks = getDbBooks();
        testComments = getDbComments(testBooks);
    }

    @Test
    @DisplayName("Загружать все комментарии по ID книги")
    void findAllByBookId() {
        for (var expectedBook : testBooks) {
            var bookId = expectedBook.getId();
            var expectedComments = testComments.stream()
                                               .filter(c -> c.getBook()
                                                             .getId()
                                                             .equals(bookId))
                                               .toList();
            var expectedCount = expectedComments.size();
            var comments = commentsService.findAllByBookId(bookId);

            assertThat(comments).hasSize(expectedCount)
                                .usingRecursiveComparison()
                                .ignoringExpectedNullFields()
                                .isEqualTo(expectedComments);
        }
    }
}
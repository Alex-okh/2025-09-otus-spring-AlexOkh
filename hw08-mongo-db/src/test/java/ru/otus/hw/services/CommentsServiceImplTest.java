package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4)
                        .boxed()
                        .map(id -> new Author(String.valueOf(id), "Author_" + id))
                        .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7)
                        .boxed()
                        .map(id -> new Genre(String.valueOf(id), "Genre_" + id))
                        .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4)
                        .boxed()
                        .map(id -> new Book(String.valueOf(id), "BookTitle_" + id, dbAuthors.get(id - 1),
                                            dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)))
                        .toList();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

    private static List<Comment> getDbComments() {
        var books = getDbBooks();
        var commentsNoBooks = IntStream.range(1, 8)
                                       .boxed()
                                       .map(id -> new Comment("Comment1_" + id, null))
                                       .toList();
        commentsNoBooks.get(0)
                       .setBook(books.get(0));
        commentsNoBooks.get(1)
                       .setBook(books.get(0));
        commentsNoBooks.get(2)
                       .setBook(books.get(2));
        return commentsNoBooks;
    }

    @BeforeEach
    void Setup() {
        testComments = getDbComments();
    }

    @ParameterizedTest
    @DisplayName("Загружать все комментарии по ID книги")
    @MethodSource("getDbBooks")
    void findAllByBookId(Book expectedBook) {
        var bookId = expectedBook.getId();
        var expectedComments = testComments.stream()
                                           .filter(c -> c.getBook()
                                                         .getId() == bookId)
                                           .toList();
        var expectedCount = expectedComments.size();
        var comments = commentsService.findAllByBookId(bookId);

        assertThat(comments).hasSize(expectedCount);
    }

    @ParameterizedTest
    @DisplayName("Загружать комментарий по ID")
    @MethodSource("getDbComments")
    void findById(Comment expectedComment) {
        var comment = commentsService.findById(expectedComment.getId());
        assertThat(comment).isPresent();
        var actualComment = comment.get();
        assertThat(actualComment).usingRecursiveComparison()
                                 .comparingOnlyFields("id", "text")
                                 .isEqualTo(expectedComment);
    }
}
package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
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

    private static List<Comment> getDbcomments() {
        var books = getDbBooks();
        return getDbComments(books);
    }

    private static List<Comment> getDbComments(List<Book> books) {
        return IntStream.range(0, 8)
                        .boxed()
                        .map(id -> new Comment("c_id_" + id, "Comment_" + ((id % 3) + 1) + "_" + (id + 1),
                                               books.get(id % 3)))
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

    @Test
    @DisplayName("Возвращать пустой лист при поиске по несуществующему ID книги")
    void findAllByBookIdFail() {
        for (var expectedBook : testBooks) {
            var bookId = expectedBook.getId()+"_1";
            var comments = commentsService.findAllByBookId(bookId);
            assertThat(comments).isEmpty();

        }
    }

    @ParameterizedTest
    @DisplayName("Загружать комментарий по ID")
    @MethodSource("getDbcomments")
    void findCommentById(Comment expectedComment) {
        var actualComment = commentsService.findCommentById(expectedComment.getId());
        assertThat(actualComment).isPresent()
                                 .get()
                                 .usingRecursiveComparison()
                                 .ignoringExpectedNullFields()
                                 .isEqualTo(expectedComment);
    }

    @DirtiesContext
    @Test
    @DisplayName("Удалять комментарий по его id")
    void deleteCommentById() {
        for (var comment : testComments) {
            assertThat(commentsService.findCommentById(comment.getId())).isPresent();
            commentsService.deleteCommentById(comment.getId());
            assertThat(commentsService.findCommentById(comment.getId())).isEmpty();
        }
    }

    @DirtiesContext
    @ParameterizedTest
    @DisplayName("Обновлять комментарий по его id")
    @MethodSource("getDbcomments")
    void updateCommentById(Comment comment) {
        assertThat(commentsService.findCommentById(comment.getId())).isPresent()
                                                                    .get()
                                                                    .usingRecursiveComparison()
                                                                    .ignoringExpectedNullFields()
                                                                    .isEqualTo(comment);

        var expectedText = comment.getText() + "_UPDATED";
        comment.setText(expectedText);
        var updatedComment = commentsService.updateCommentById(expectedText, comment.getId());
        var actualComment = commentsService.findCommentById(comment.getId());
        assertThat(actualComment).isPresent()
                                 .get()
                                 .usingRecursiveComparison()
                                 .ignoringExpectedNullFields()
                                 .isEqualTo(comment);
        assertThat(updatedComment).usingRecursiveComparison()
                                  .ignoringExpectedNullFields()
                                  .isEqualTo(comment);
    }

    @DirtiesContext
    @ParameterizedTest
    @DisplayName("Создавать комментарий по id книги")
    @MethodSource("getDbBooks")
    void createCommentById(Book book) {
        var expectedComments = commentsService.findAllByBookId(book.getId());
        var newComment = new Comment(null, "NEW_COMMENT FOR BOOK" + book.getTitle(), book);
        expectedComments.add(newComment);

        commentsService.createCommentByBookId(newComment.getText(), book.getId());

        var actualComments = commentsService.findAllByBookId(book.getId());

        assertThat(actualComments).usingRecursiveComparison()
                                  .ignoringFields("id")
                                  .ignoringExpectedNullFields()
                                  .isEqualTo(expectedComments);

    }
}


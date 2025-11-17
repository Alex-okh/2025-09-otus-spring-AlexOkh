package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.repositories.JPACommentRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("Сервис комментариев должен")
@DataJpaTest
@Import({CommentsServiceImpl.class, JPACommentRepository.class})
@Transactional(propagation = Propagation.NEVER)
class CommentsServiceImplTest {

    @Autowired
    private CommentsService commentsService;

    @ParameterizedTest
    @DisplayName("Загружать все комментарии по ID книги")
    @CsvSource({"1,2", "2,0", "3,1"})
    void findAllByBookId(long bookId, int expectedCount) {
        var comments = commentsService.findAllByBookId(bookId);
        assertThat(comments).hasSize(expectedCount);
        comments.forEach(comment -> {
            assertThatCode(() -> {
                assertThat(comment.getBook()
                                  .getId()).isEqualTo(bookId);
                assertThat(comment.getBook()
                                  .getGenres()).isNotEmpty();
                assertThat(comment.getBook()
                                  .getAuthor()).isNotNull();
            }).doesNotThrowAnyException();

        });
    }

    @ParameterizedTest
    @DisplayName("Загружать комментарий по ID")
    @CsvSource({"1, Comment1_1", "2, Comment1_2", "3, Comment1_3"})
    void findById(long id, String text) {
        var comment = commentsService.findById(id);
        assertThat(comment).isNotEmpty()
                           .get()
                           .hasFieldOrPropertyWithValue("text", text);
        assertThatCode(() -> {
            assertThat(comment.get()
                              .getBook()
                              .getGenres()).isNotEmpty();
            assertThat(comment.get()
                              .getBook()
                              .getAuthor()).isNotNull();
        }).doesNotThrowAnyException();
    }
}
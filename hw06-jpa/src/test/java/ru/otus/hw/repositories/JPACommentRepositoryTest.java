package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("репозиторий для работы с комментариями должен:")
@DataJpaTest
@Import(JPACommentRepository.class)
public class JPACommentRepositoryTest {

    private static final long GOOD_COMMENT_ID = 1L;
    private static final String GOOD_COMMENT_TEXT = "Comment1_1";

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен получать комментарий по ID")
    @Test
    void shouldLoadCommentById() {
        var actualComment = commentRepository.findById(1L);
        assertThat(actualComment).isNotEmpty()
                                 .get()
                                 .hasFieldOrPropertyWithValue("text", GOOD_COMMENT_TEXT)
                                 .hasFieldOrPropertyWithValue("id", GOOD_COMMENT_ID);

    }

    @DisplayName("должен получать комментарии по ID книги")
    @Test
    void shouldLoadCommentByBookId() {
        var actualComments = commentRepository.findByBookId(1L);
        assertThat(actualComments).hasSize(2);
    }
}

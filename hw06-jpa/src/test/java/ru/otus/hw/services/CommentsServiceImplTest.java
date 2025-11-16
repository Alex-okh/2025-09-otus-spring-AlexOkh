package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис комментариев должен")
@SpringBootTest
class CommentsServiceImplTest {

    @Autowired
    private CommentsService commentsService;

    @ParameterizedTest
    @DisplayName("Загружать все комментарии по ID книги")
    @CsvSource({"1,2", "2,0", "3,1"})
    void findAllByBookId(long bookId, int expectedCount) {
        var comments = commentsService.FindAllByBookId(bookId);
        assertThat(comments).hasSize(expectedCount);
    }

    @ParameterizedTest
    @DisplayName("Загружать комментарий по ID")
    @CsvSource({"1, Comment1_1", "2, Comment1_2", "3, Comment1_3"})
    void findById(long id, String text) {
        var comment = commentsService.FindById(id);
        assertThat(comment).isNotEmpty()
                           .get()
                           .hasFieldOrPropertyWithValue("text", text);
    }
}
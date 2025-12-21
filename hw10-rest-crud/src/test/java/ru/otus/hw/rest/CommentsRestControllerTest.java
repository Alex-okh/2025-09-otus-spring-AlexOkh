package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.NewCommentDto;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.services.CommentsService;
import ru.otus.hw.util.TestDataGenerator;
import java.util.List;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({CommentsRestController.class, CommentMapper.class})
@DisplayName("API комментариев должен: ")
class CommentsRestControllerTest {

    @Autowired
    CommentMapper cm;
    private List<CommentDto> comments;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CommentsService commentsService;

    @BeforeEach
    void setUp() {
        comments = cm.commentToDto(TestDataGenerator.getDbcomments());
    }

    @DisplayName("Возвращать массив комментариев по id книги")
    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3})
    void getCommentsByBookId(Long bookId) throws Exception {
        var expectedComments = comments.stream()
                                       .filter(c -> c.bookId() == bookId)
                                       .toList();

        when(commentsService.findAllByBookId(bookId)).thenReturn(expectedComments);

        mvc.perform(get("/api/books/{bookId}/comments", bookId))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(content().json(objectMapper.writeValueAsString(expectedComments)));

        verify(commentsService).findAllByBookId(bookId);
    }

    @DisplayName("Возвращать пустой массив комментариев по id если книги или комментариев нет")
    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3})
    void getEmptyCommentsByBookId(Long bookId) throws Exception {
        when(commentsService.findAllByBookId(bookId)).thenReturn(List.of());

        mvc.perform(get("/api/books/{bookId}/comments", bookId))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(content().json("[]"));

        verify(commentsService).findAllByBookId(bookId);
    }

    @DisplayName("Сохранять комментарий по id книги")
    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3})
    void saveCommentByBookId(Long bookId) throws Exception {
        var incomingComment = new CommentDto(0, "New Comment", bookId);
        var newComment = new NewCommentDto(bookId, "New Comment");

        when(commentsService.save(newComment)).thenReturn(incomingComment);

        mvc.perform(post("/api/books/{bookId}/comments", bookId).contentType(MediaType.APPLICATION_JSON)
                                                                .content(objectMapper.writeValueAsString(
                                                                        incomingComment)))
           .andExpect(status().isCreated());

        verify(commentsService).save(newComment);
    }

    @DisplayName("Возвращать 404 по id несуществующей книги")
    @ParameterizedTest
    @ValueSource(longs = {4, 5, 6})
    void saveCommentByBadBookId(Long bookId) throws Exception {
        var incomingComment = new CommentDto(0, "New Comment", bookId);
        var newComment = new NewCommentDto(bookId, "New Comment");

        when(commentsService.save(newComment)).thenThrow(
                new BookNotFoundException("Book with id %d not found".formatted(bookId)));

        mvc.perform(post("/api/books/{bookId}/comments", bookId).contentType(MediaType.APPLICATION_JSON)
                                                                .content(objectMapper.writeValueAsString(
                                                                        incomingComment)))
           .andExpect(status().isNotFound());
        verify(commentsService).save(newComment);
    }
}

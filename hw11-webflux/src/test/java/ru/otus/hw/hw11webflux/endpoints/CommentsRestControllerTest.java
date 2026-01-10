package ru.otus.hw.hw11webflux.endpoints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.hw11webflux.controllers.CommentController;
import ru.otus.hw.hw11webflux.controllers.GlobalExceptionHandler;
import ru.otus.hw.hw11webflux.dto.CommentDto;
import ru.otus.hw.hw11webflux.exceptions.EntityNotFoundException;
import ru.otus.hw.hw11webflux.mappers.CommentMapper;
import ru.otus.hw.hw11webflux.service.CommentService;
import ru.otus.hw.hw11webflux.util.TestDataGenerator;
import java.util.List;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = CommentController.class)
@Import({CommentMapper.class, GlobalExceptionHandler.class})
@DisplayName("API комментариев должен:")
class CommentsRestControllerTest {

    @Autowired
    CommentMapper cm;
    private List<CommentDto> comments;

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CommentService commentsService;

    //
    @BeforeEach
    void setUp() {
        comments = cm.toDto(TestDataGenerator.getDbcomments());
    }

    //
    @DisplayName("Возвращать массив комментариев по id книги")
    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3"})
    void getCommentsByBookId(String bookId) {
        var expectedComments = comments.stream()
                                       .filter(c -> c.bookId()
                                                     .equals(bookId))
                                       .toList();

        when(commentsService.findAllByBookId(bookId)).thenReturn(Flux.fromIterable(expectedComments));

        webTestClient.get()
                     .uri("/api/flux/books/{bookId}/comments", bookId)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBodyList(CommentDto.class)
                     .isEqualTo(expectedComments);

        verify(commentsService).findAllByBookId(bookId);
    }

    @DisplayName("Возвращать пустой массив комментариев по id книги если их нет")
    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3"})
    void getEmptyCommentsByBookId(String bookId) {

        when(commentsService.findAllByBookId(bookId)).thenReturn(Flux.empty());

        webTestClient.get()
                     .uri("/api/flux/books/{bookId}/comments", bookId)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBodyList(CommentDto.class)
                     .hasSize(0);

        verify(commentsService).findAllByBookId(bookId);
    }

    @DisplayName("Возвращать 404 если книги нет при сохранении")
    @ParameterizedTest
    @ValueSource(strings = {"4", "5", "6"})
    void saveCommentsByBadBookId(String bookId) {
        var expectedComment = new CommentDto(null, "NEW_COMMENT", bookId);
        when(commentsService.saveComment(expectedComment)).thenReturn(
                Mono.error(new EntityNotFoundException("Book with Id %s not found".formatted(bookId))));

        webTestClient.post()
                     .uri("/api/flux/books/{bookId}/comments", bookId)
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(expectedComment)
                     .exchange()
                     .expectStatus()
                     .isNotFound();

        verify(commentsService).saveComment(expectedComment);
    }

    @DisplayName("Возвращать сохраненный комментарий")
    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3"})
    void saveCommentsByBookId(String bookId) {
        var expectedComment = new CommentDto(null, "NEW_COMMENT", bookId);
        var returnedComment = new CommentDto("new_id", "NEW_COMMENT", bookId);
        when(commentsService.saveComment(expectedComment)).thenReturn(Mono.just(returnedComment));

        webTestClient.post()
                     .uri("/api/flux/books/{bookId}/comments", bookId)
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(expectedComment)
                     .exchange()
                     .expectStatus()
                     .isCreated()
                     .expectBody()
                     .equals(returnedComment);

        verify(commentsService).saveComment(expectedComment);
    }
}


package ru.otus.hw.hw11webflux.endpoints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.hw11webflux.controllers.BookController;
import ru.otus.hw.hw11webflux.dto.AuthorDto;
import ru.otus.hw.hw11webflux.dto.BookDto;
import ru.otus.hw.hw11webflux.dto.GenreDto;
import ru.otus.hw.hw11webflux.dto.NewBookDto;
import ru.otus.hw.hw11webflux.exceptions.EntityNotFoundException;
import ru.otus.hw.hw11webflux.mappers.AuthorMapper;
import ru.otus.hw.hw11webflux.mappers.BookMapper;
import ru.otus.hw.hw11webflux.mappers.GenreMapper;
import ru.otus.hw.hw11webflux.service.BookService;
import ru.otus.hw.hw11webflux.util.TestDataGenerator;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = BookController.class)
@Import({BookMapper.class, GenreMapper.class, AuthorMapper.class})
@DisplayName("API книг должен:")
class BooksRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    private List<BookDto> expectedBooks;

    @Autowired
    private BookMapper bookMapper;

    @MockitoBean
    private BookService bookService;

    @BeforeEach
    void setUp() {
        expectedBooks = bookMapper.toDto(TestDataGenerator.getDbBooks());
    }

    @DisplayName("Возвращать список книг при запросе /api/flux/books")
    @Test
    void getAllBooks() {
        when(bookService.getAllBooks()).thenReturn(Flux.fromIterable(expectedBooks));
        webTestClient.get()
                     .uri("/api/flux/books")
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBodyList(BookDto.class)
                     .isEqualTo(expectedBooks);

        verify(bookService).getAllBooks();
    }

    @DisplayName("Возвращать пустой массив при запросе /api/books если книг нет")
    @Test
    void getAllBooksEmptyTest() {
        when(bookService.getAllBooks()).thenReturn(Flux.empty());
        webTestClient.get()
                     .uri("/api/flux/books")
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBodyList(BookDto.class)
                     .hasSize(0);
        verify(bookService).getAllBooks();
    }

    @DisplayName("Возвращать книгу по id")
    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3"})
    void getBook(String bookId) {
        var expectedBook = expectedBooks.stream()
                                        .filter(b -> b.id()
                                                      .equals(bookId))
                                        .findFirst()
                                        .get();

        when(bookService.getBook(bookId)).thenReturn(Mono.just(expectedBook));
        webTestClient.get()
                     .uri("/api/flux/books/{bookId}", bookId)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBodyList(BookDto.class)
                     .equals(expectedBook);

        verify(bookService).getBook(bookId);
    }

    @DisplayName("Возвращать 404 по валидному несуществующему id")
    @ParameterizedTest
    @ValueSource(strings = {"4", "5", "6"})
    void getBookBadTest(String bookId) {

        when(bookService.getBook(bookId)).thenReturn(
                Mono.error(new EntityNotFoundException("Book with Id %s not found".formatted(bookId))));

        webTestClient.get()
                     .uri("/api/flux/books/{bookId}", bookId)
                     .exchange()
                     .expectStatus()
                     .isNotFound();

        verify(bookService).getBook(bookId);
    }

    @DisplayName("Удалять по id независимо от наличия")
    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3", "4", "5", "6"})
    void deleteBookTest(String bookId) {
        webTestClient.delete()
                     .uri("/api/flux/books/{bookId}", bookId)
                     .exchange()
                     .expectStatus()
                     .isNoContent();
        verify(bookService).deleteBook(bookId);
    }

    @DisplayName("Создавать книгу по POST")
    @Test
    void postBook() {
        var expectedNewBook = new NewBookDto("New book title", "2", Set.of("1", "2"));
        var newAuthor = new AuthorDto("2", "Author_2");
        var newGenres = new HashSet<GenreDto>();
        newGenres.add(new GenreDto("1", "Genre_1"));
        newGenres.add(new GenreDto("2", "Genre_2"));
        var expectedCreatedBook = new BookDto("33", "New book title", newAuthor, newGenres);

        when(bookService.addBook(expectedNewBook)).thenReturn(Mono.just(expectedCreatedBook));
//
        webTestClient.post()
                     .uri("/api/flux/books")
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(expectedNewBook)
                     .exchange()
                     .expectStatus()
                     .isCreated()
                     .expectBodyList(BookDto.class)
                     .equals(expectedCreatedBook);
//
        verify(bookService).addBook(expectedNewBook);
    }

    //
    @DisplayName("Обновлять книгу по PUT")
    @Test
    void putBook() {
        String updateId = "3";
        var expectedUpdateBook = new NewBookDto("New book title", "2", Set.of("1", "2"));
        var newAuthor = new AuthorDto("2", "Author_2");
        var newGenres = new HashSet<GenreDto>();
        newGenres.add(new GenreDto("1", "Genre_1"));
        newGenres.add(new GenreDto("2", "Genre_2"));

        var expectedCreatedBook = new BookDto(updateId, "New book title", newAuthor, newGenres);

        when(bookService.updateBook(updateId, expectedUpdateBook)).thenReturn(Mono.just(expectedCreatedBook));

        webTestClient.put()
                     .uri("/api/flux/books/{updateId}", updateId)
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(expectedUpdateBook)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBodyList(BookDto.class)
                     .equals(expectedCreatedBook);

        verify(bookService).updateBook(updateId, expectedUpdateBook);
    }

    @DisplayName("Возвращать 400 при POST с ошибкой валидации")
    @ParameterizedTest
    @CsvSource(delimiter = '|',
               nullValues = "NULL",
               value = {"|1|1,2", "New book|NULL|1,2", "New book|1|NULL"})
    void postBadBook(String postedTitle, String postedAuthorId, String postedGenreIds) {
        Set<String> genreIds = null;
        if (postedGenreIds != null) {
            genreIds = Arrays.stream(postedGenreIds.split(","))
                             .collect(Collectors.toSet());
        }
        var expectedNewBook = new NewBookDto(postedTitle, postedAuthorId, genreIds);
        webTestClient.post()
                     .uri("/api/flux/books")
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(expectedNewBook)
                     .exchange()
                     .expectStatus()
                     .isBadRequest();

//
        verify(bookService, never()).addBook(any());
    }

    //
    @DisplayName("Возвращать 400 при PUT с ошибкой валидации")
    @ParameterizedTest
    @CsvSource(delimiter = '|',
               nullValues = "NULL",
               value = {"1||1|1,2", "1|New book|NULL|1,2", "1 | New book|1|NULL"})
    void putBadBook(String postedBookId, String postedTitle, String postedAuthorId,
                    String postedGenreIds) {
        Set<String> genreIds = null;
        if (postedGenreIds != null) {
            genreIds = Arrays.stream(postedGenreIds.split(","))
                             .collect(Collectors.toSet());
        }
        var expectedNewBook = new NewBookDto(postedTitle, postedAuthorId, genreIds);

        webTestClient.put()
                     .uri("/api/flux/books/{postedBookId}", postedBookId)
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(expectedNewBook)
                     .exchange()
                     .expectStatus()
                     .isBadRequest();

        verify(bookService, never()).updateBook(any(), any());
    }

    @DisplayName("Возвращать 500 при прочих ошибках")
    @Test
    void putSomethingWrong() {
        String updateId = "3";
        var expectedNewBook = new NewBookDto("New book title", "2", Set.of("1", "2"));
        when(bookService.updateBook(any(), any())).thenThrow(new ArithmeticException());

        webTestClient.put()
                     .uri("/api/flux/books/{postedBookId}", updateId)
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(expectedNewBook)
                     .exchange()
                     .expectStatus()
                     .is5xxServerError();

        verify(bookService).updateBook(any(), any());
    }
}

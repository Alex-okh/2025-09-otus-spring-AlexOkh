package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.NewBookDto;
import ru.otus.hw.dto.UpdateBookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.services.BookService;
import ru.otus.hw.util.TestDataGenerator;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({BooksRestController.class, BookMapper.class})
@DisplayName("API книг должен: ")
class BooksRestControllerTest {

    private List<BookDto> expectedBooks;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    @BeforeEach
    void setUp() {
        expectedBooks = bookMapper.bookToDto(TestDataGenerator.getDbBooks());
    }

    @DisplayName("Возвращать список книг при запросе /api/books")
    @Test
    void getAllBooks() throws Exception {
        when(bookService.findAll()).thenReturn(expectedBooks);

        mvc.perform(get("/api/books"))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(content().json(objectMapper.writeValueAsString(expectedBooks)));

        verify(bookService).findAll();
    }

    @DisplayName("Возвращать пустой массив при запросе /api/books если книг нет")
    @Test
    void getAllBooksEmptyTest() throws Exception {
        when(bookService.findAll()).thenReturn(List.of());

        mvc.perform(get("/api/books"))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(content().json("[]"));

        verify(bookService).findAll();
    }

    @DisplayName("Возвращать книгу по id")
    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3})
    void getBook(Long bookId) throws Exception {
        var expectedBook = expectedBooks.stream()
                                        .filter(b -> b.id()
                                                      .equals(bookId))
                                        .findFirst()
                                        .get();

        when(bookService.findById(bookId)).thenReturn(expectedBook);

        mvc.perform(get("/api/books/{bookId}", bookId).contentType(MediaType.APPLICATION_JSON)
                                                      .content(objectMapper.writeValueAsString(expectedBook)))
           .andExpect(status().isOk());

        verify(bookService).findById(bookId);
    }

    @DisplayName("Возвращать 400 по валидному несуществующему id")
    @ParameterizedTest
    @ValueSource(longs = {4, 5, 6})
    void getBookBadTest(Long bookId) throws Exception {

        when(bookService.findById(bookId)).thenThrow(
                new EntityNotFoundException("Book with id %d not found".formatted(bookId)));

        mvc.perform(get("/api/books/{bookId}", bookId))
           .andExpect(status().isBadRequest());
        verify(bookService).findById(bookId);
    }

    @DisplayName("Удалять по id независимо от наличия")
    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4, 5, 6})
    void deleteBookTest(Long bookId) throws Exception {
        mvc.perform(delete("/api/books/{bookId}", bookId))
           .andExpect(status().isNoContent());
        verify(bookService).deleteById(bookId);
    }

    @DisplayName("Создавать книгу по POST")
    @Test
    void postBook() throws Exception {
        var expectedNewBook = new NewBookDto("New book title", 2L, Set.of(1L, 2L));
        var expectedCreatedBook = new BookDto(33L, "New book title", "Author", 2L, "GenresString", Set.of(1L, 2L));

        when(bookService.create(expectedNewBook)).thenReturn(expectedCreatedBook);

        mvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON)
                                      .content(objectMapper.writeValueAsString(expectedNewBook)))
           .andExpect(status().isCreated())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(content().json(objectMapper.writeValueAsString(expectedCreatedBook)));

        verify(bookService).create(expectedNewBook);
    }

    @DisplayName("Обновлять книгу по PUT")
    @Test
    void putBook() throws Exception {
        var expectedUpdateBook = new UpdateBookDto(3L, "New book title", 2L, Set.of(1L, 2L));
        var expectedCreatedBook = new BookDto(3L, "New book title", "Author", 2L, "GenresString", Set.of(1L, 2L));

        when(bookService.update(expectedUpdateBook)).thenReturn(expectedCreatedBook);

        mvc.perform(put("/api/books").contentType(MediaType.APPLICATION_JSON)
                                     .content(objectMapper.writeValueAsString(expectedUpdateBook)))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(content().json(objectMapper.writeValueAsString(expectedCreatedBook)));

        verify(bookService).update(expectedUpdateBook);
    }

    @DisplayName("Возвращать 400 при POST с ошибкой валидации")
    @ParameterizedTest
    @CsvSource(delimiter = '|',
               nullValues = "NULL",
               value = {"|1|1,2", "New book|NULL|1,2", "New book|1|NULL"})
    void postBadBook(String postedTitle, Long postedAuthorId, String postedGenreIds) throws Exception {
        Set<Long> genreIds = null;
        if (postedGenreIds != null) {
            genreIds = Arrays.stream(postedGenreIds.split(","))
                             .mapToLong(Long::parseLong)
                             .boxed()
                             .collect(Collectors.toSet());
        }
        var expectedNewBook = new NewBookDto(postedTitle, postedAuthorId, genreIds);
        mvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON)
                                      .content(objectMapper.writeValueAsString(expectedNewBook)))
           .andExpect(status().isBadRequest());

        verify(bookService, never()).create(any());
    }

    @DisplayName("Возвращать 400 при PUT с ошибкой валидации")
    @ParameterizedTest
    @CsvSource(delimiter = '|',
               nullValues = "NULL",
               value = {"0|New book|1|1,2", "1||1|1,2", "1|New book|NULL|1,2", "1 | New book|1|NULL"})
    void putBadBook(Long postedBookId, String postedTitle, Long postedAuthorId,
                    String postedGenreIds) throws Exception {
        Set<Long> genreIds = null;
        if (postedGenreIds != null) {
            genreIds = Arrays.stream(postedGenreIds.split(","))
                             .mapToLong(Long::parseLong)
                             .boxed()
                             .collect(Collectors.toSet());
        }
        var expectedUpdateBook = new UpdateBookDto(postedBookId, postedTitle, postedAuthorId, genreIds);
        mvc.perform(put("/api/books").contentType(MediaType.APPLICATION_JSON)
                                     .content(objectMapper.writeValueAsString(expectedUpdateBook)))
           .andExpect(status().isBadRequest());

        verify(bookService, never()).update(any());
    }

    @DisplayName("Возвращать 500 при прочих ошибках")
    @Test
    void putSomethingWrong() throws Exception {
        when(bookService.update(any())).thenThrow(new ArithmeticException());

        var expectedUpdateBook = new UpdateBookDto(2L, "TEST", 1L, Set.of(1L, 2L));
        mvc.perform(put("/api/books").contentType(MediaType.APPLICATION_JSON)
                                     .content(objectMapper.writeValueAsString(expectedUpdateBook)))
           .andExpect(status().isInternalServerError());

        verify(bookService, never()).create(any());
    }
}

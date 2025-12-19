package ru.otus.hw.restcontrollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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
import java.util.List;
import java.util.Set;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({BooksApi.class, BookMapper.class})
@DisplayName("API книг должен: ")
class BooksApiTest {

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

    @DisplayName("Возвращать 404 по несуществующему id")
    @ParameterizedTest
    @ValueSource(longs = {4, 5, 6})
    void getBookBadTest(Long bookId) throws Exception {

        when(bookService.findById(bookId)).thenThrow(
                new EntityNotFoundException("Book with id %d not found".formatted(bookId)));

        mvc.perform(get("/api/books/{bookId}", bookId))
           .andExpect(status().isNotFound());
        verify(bookService).findById(bookId);
    }

    @DisplayName("Удалять по id независимо от наличия")
    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4, 5, 6})
    void deleteBookTest(Long bookId) throws Exception {
        mvc.perform(delete("/api/books/{bookId}", bookId))
           .andExpect(status().isOk());
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

}

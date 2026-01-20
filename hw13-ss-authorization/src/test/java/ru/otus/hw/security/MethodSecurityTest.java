package ru.otus.hw.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.BookServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {BookServiceImpl.class, BookMapper.class, AuthorMapper.class, GenreMapper.class})
@EnableMethodSecurity

@DisplayName("При обращении к мутирующим методам")
public class MethodSecurityTest {
    private Author expectedAuthor;
    private List<Genre> expectedGenres;
    private BookDto expectedBookDto;
    private Book expectedBook;

    @MockitoBean
    private AuthorRepository authorRepository;

    @MockitoBean
    private GenreRepository genreRepository;

    @MockitoBean
    private BookRepository bookRepository;

    @MockitoBean
    private BookMapper bookMapper;

    @Autowired
    private BookService bookService;

    @BeforeEach
    void SetUp() {
        expectedAuthor = new Author(1L, "TestAuthor");
        expectedGenres = List.of(new Genre(1L, "TestGenre"));
        expectedBookDto = new BookDto(1L, "TestTitle", null, 1L, null, Set.of(1L), "editor1");
        expectedBook = new Book(1L, "TestTitle", expectedAuthor, expectedGenres, "editor1");
    }

    @DisplayName("Создавать книгу с ролью EDITOR")
    @Test
    @WithMockUser(authorities = "EDITOR")
    void createTest() {
        when(bookRepository.save(expectedBook)).thenReturn(expectedBook);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(expectedAuthor));
        when(genreRepository.findAllById(Set.of(1L))).thenReturn(expectedGenres);
        assertDoesNotThrow(() -> bookService.create(expectedBookDto));
    }

    @DisplayName("Выбрасывать исключение с ролью USER")
    @Test
    @WithMockUser(authorities = "USER")
    void createByUser() {
        when(bookRepository.save(expectedBook)).thenReturn(expectedBook);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(expectedAuthor));
        when(genreRepository.findAllById(Set.of(1L))).thenReturn(expectedGenres);
        assertThrows(AuthorizationDeniedException.class, () -> bookService.create(expectedBookDto));
    }

    @DisplayName("Выбрасывать исключение без аутентификации")
    @Test
    void createByAnonimous() {
        when(bookRepository.save(expectedBook)).thenReturn(expectedBook);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(expectedAuthor));
        when(genreRepository.findAllById(Set.of(1L))).thenReturn(expectedGenres);
        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> bookService.create(expectedBookDto));
    }

    @DisplayName("Удалять книгу с ролью EDITOR")
    @Test
    @WithMockUser(authorities = "EDITOR")
    void deleteTest() {
        assertDoesNotThrow(() -> bookService.deleteById(1L));
    }

    @DisplayName("Выбрасывать исключение с ролью USER")
    @Test
    @WithMockUser(authorities = "USER")
    void deleteByUser() {
        assertThrows(AuthorizationDeniedException.class, () -> bookService.deleteById(1L));
    }

    @DisplayName("Выбрасывать исключение без аутентификации")
    @Test
    void deleteByAnonimous() {
        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> bookService.deleteById(1L));
    }

    @DisplayName("Редактировать книгу с ролью EDITOR")
    @Test
    @WithMockUser(authorities = "EDITOR", username = "editor1")
    void updateTest() {
        when(bookRepository.save(expectedBook)).thenReturn(expectedBook);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(expectedBook));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(expectedAuthor));
        when(genreRepository.findAllById(Set.of(1L))).thenReturn(expectedGenres);
        System.out.println(bookService.update(expectedBookDto));
        assertDoesNotThrow(() -> bookService.update(expectedBookDto));
    }

    @DisplayName("Выбрасывать исключение с ролью USER")
    @Test
    @WithMockUser(authorities = "USER")
    void updateByUser() {
        when(bookRepository.save(expectedBook)).thenReturn(expectedBook);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(expectedAuthor));
        when(genreRepository.findAllById(Set.of(1L))).thenReturn(expectedGenres);
        assertThrows(AuthorizationDeniedException.class, () -> bookService.update(expectedBookDto));

    }

    @DisplayName("Выбрасывать исключение без аутентификации")
    @Test
    void updateByAnonimous() {
        when(bookRepository.save(expectedBook)).thenReturn(expectedBook);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(expectedAuthor));
        when(genreRepository.findAllById(Set.of(1L))).thenReturn(expectedGenres);
        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> bookService.update(expectedBookDto));
    }
}


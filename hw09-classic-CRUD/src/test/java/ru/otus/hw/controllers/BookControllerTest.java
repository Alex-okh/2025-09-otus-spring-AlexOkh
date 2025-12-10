package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.NewCommentDto;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentsService;
import ru.otus.hw.services.GenreService;
import ru.otus.hw.util.TestDataGenerator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("Контроллер книг должен:")
@WebMvcTest({BookController.class, BookMapper.class, AuthorMapper.class, GenreMapper.class, CommentMapper.class})
class BookControllerTest {
    private List<Book> dbBooks;

    private List<Comment> dbComments;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AuthorMapper authorMapper;

    @Autowired
    private GenreMapper genreMapper;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private CommentMapper commentMapper;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private AuthorService authorService;

    @MockitoBean
    private CommentsService commentsService;

    @MockitoBean
    private GenreService genreService;

    @BeforeEach
    void setUp() {
        dbBooks = TestDataGenerator.getDbBooks();
        dbComments = TestDataGenerator.getDbcomments();
        dbAuthors = TestDataGenerator.getDbAuthors();
        dbGenres = TestDataGenerator.getDbGenres();
    }

    @Test
    @DisplayName("Формировать страницу с ожидаемым списком при GET /books")
    void showAllBooks() throws Exception {
        var expectedBooksCount = dbBooks.size();
        var expectedBookNames = dbBooks.stream()
                                       .map(Book::getTitle)
                                       .toList();

        when(bookService.findAll()).thenReturn(bookMapper.bookToDto(dbBooks));

        this.mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", hasSize(expectedBooksCount)))
                .andExpect(content().string(stringContainsInOrder(expectedBookNames)));
    }

    @Test
    @DisplayName("Показывать страницу редактирования для новой книги")
    void createBook() throws Exception {
        when(authorService.findAll()).thenReturn(authorMapper.authorToDto(dbAuthors));
        when(genreService.findAll()).thenReturn(genreMapper.genreToDto(dbGenres));

        this.mvc.perform(get("/books/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("editbook"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("authors", hasSize(dbAuthors.size())))
                .andExpect(model().attribute("genres", hasSize(dbGenres.size())));

    }

    @ParameterizedTest
    @DisplayName("Показывать страницу редактирования")
    @MethodSource("ru.otus.hw.util.TestDataGenerator#getDbBooks")
    void editBook(Book expectedBook) throws Exception {
        when(bookService.findById(expectedBook.getId())).thenReturn(bookMapper.bookToDto(expectedBook));
        when(authorService.findAll()).thenReturn(authorMapper.authorToDto(dbAuthors));
        when(genreService.findAll()).thenReturn(genreMapper.genreToDto(dbGenres));

        this.mvc.perform(get("/books/" + expectedBook.getId() + "/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("editbook"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("authors", hasSize(dbAuthors.size())))
                .andExpect(model().attribute("genres", hasSize(dbGenres.size())))
                .andExpect(content().string(containsString(expectedBook.getTitle())))
                .andExpect(content().string(containsString(expectedBook.getAuthor()
                                                                       .getFullName())));

        verify(bookService).findById(expectedBook.getId());
    }

    @ParameterizedTest
    @DisplayName("Выводить книгу с комментариями")
    @MethodSource("ru.otus.hw.util.TestDataGenerator#getDbBooks")
    void showBook(Book expectedBook) throws Exception {
        var expectedComments = dbComments.stream()
                                         .filter(c -> c.getBook()
                                                       .getId() == expectedBook.getId())
                                         .toList();
        var expectedCommentTexts = expectedComments.stream()
                                                   .map(Comment::getText)
                                                   .toList();

        when(bookService.findById(expectedBook.getId())).thenReturn(bookMapper.bookToDto(expectedBook));
        when(commentsService.findAllByBookId(expectedBook.getId())).thenReturn(commentMapper.commentToDto(expectedComments));

        this.mvc.perform(get("/book/" + expectedBook.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("book"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("comments"))
                .andExpect(model().attribute("comments", hasSize(expectedComments.size())))
                .andExpect(content().string(stringContainsInOrder(expectedCommentTexts)))
                .andExpect(content().string(containsString(expectedBook.getTitle())))
                .andExpect(content().string(containsString(expectedBook.getAuthor()
                                                                       .getFullName())));

        verify(bookService).findById(expectedBook.getId());
        verify(commentsService).findAllByBookId(expectedBook.getId());
    }

    @Test
    @DisplayName("Создавать новую книгу при запросе с id = 0 ")
    void updateBookNew() throws Exception {
        Long paramId = 0L;
        String paramTitle = "NEW BOOK";
        Integer paramAuthorId = 1;
        var expectedBookDto = bookMapper.bookToDto(
                new Book(0,
                         paramTitle,
                         new Author(paramAuthorId, ""),
                         List.of(new Genre(1L, ""), new Genre (2L, ""))));

        when(bookService.create(any())).thenReturn(bookMapper.bookToDto(new Book()));

        this.mvc.perform(post("/book").param("id", String.valueOf(paramId))
                                      .param("title", paramTitle)
                                      .param("authorId", String.valueOf(paramAuthorId))
                                      .param("genreIds", "1")
                                      .param("genreIds", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/books"))
                .andExpect(redirectedUrl("/books"));

        verify(bookService).create(expectedBookDto);
    }

    @Test
    @DisplayName("Обновлять  книгу при запросе с id != 0 ")
    void updateBookUpdate() throws Exception {
        Long paramId = 1L;
        String paramTitle = "NEW BOOK";
        Integer paramAuthorId = 1;
        var expectedBookDto = bookMapper.bookToDto(
                new Book(paramId,
                         paramTitle,
                         new Author(paramAuthorId, ""),
                         List.of(new Genre(1L, ""), new Genre (2L, ""))));

        when(bookService.update(any())).thenReturn(bookMapper.bookToDto(new Book()));

        this.mvc.perform(post("/book").param("id", String.valueOf(paramId))
                                      .param("title", paramTitle)
                                      .param("authorId", String.valueOf(paramAuthorId))
                                      .param("genreIds", "1")
                                      .param("genreIds", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/books"))
                .andExpect(redirectedUrl("/books"));

        verify(bookService).update(expectedBookDto);
    }

    @ParameterizedTest
    @DisplayName("Удалять книгу по ID")
    @ValueSource(longs = {1L, 2L, 3L, 15L, 100L})
    void deleteBook(Long bookId) throws Exception {
        doNothing().when(bookService)
                   .deleteById(bookId);

        this.mvc.perform(post("/books/{id}", bookId).param("_method", "DELETE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"))
                .andExpect(view().name("redirect:/books"));

        verify(bookService).deleteById(bookId);
    }

    @ParameterizedTest
    @DisplayName("Добавлять комментарий к книге")
    @MethodSource("ru.otus.hw.util.TestDataGenerator#getDbBooks")
    void saveComment(Book book) throws Exception {
        doNothing().when(commentsService)
                   .save(any(NewCommentDto.class));

        String expectedCommentText = "Новый комментарий";
        Long bookId = book.getId();
        String paramBookId = String.valueOf(bookId);
        var expectedDto = new NewCommentDto(bookId, expectedCommentText);

        this.mvc.perform(post("/book/comment").param("bookId", paramBookId)
                                              .param("text", expectedCommentText))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/" + paramBookId))
                .andExpect(view().name("redirect:/book/" + paramBookId));

        verify(commentsService).save(expectedDto);
    }
}
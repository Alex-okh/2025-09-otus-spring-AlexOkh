package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.util.TestDataGenerator;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest({AuthorController.class, AuthorMapper.class})
@DisplayName("Контроллер авторов должен")
class AuthorControllerTest {

    private List<Author> dbAuthors;

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        dbAuthors = TestDataGenerator.getDbAuthors();
    }

    @DisplayName("Формировать страницу с ожидаемым списком при GET /authors")
    @Test
    void showAuthors() throws Exception {
        var expectedAuthorsCount = dbAuthors.size();
        var expectedAuthorNames = dbAuthors.stream()
                                           .map(Author::getFullName)
                                           .toList();

        when(authorService.findAll()).thenReturn(dbAuthors);

        this.mvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(view().name("authors"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attribute("authors", hasSize(expectedAuthorsCount)))
                .andExpect(content().string(stringContainsInOrder(expectedAuthorNames)));
    }
}
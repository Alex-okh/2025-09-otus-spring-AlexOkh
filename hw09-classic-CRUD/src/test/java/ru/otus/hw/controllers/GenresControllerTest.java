package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.GenreService;
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

@WebMvcTest({GenresController.class, GenreMapper.class})
@DisplayName("Контроллер жанров должен")
class GenresControllerTest {

    private List<Genre> dbGenres;

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private GenreService genreService;

    @BeforeEach
    void setUp() {
        dbGenres = TestDataGenerator.getDbGenres();
    }

    @DisplayName("Формировать страницу с ожидаемым списком при GET /genres")
    @Test
    void showAuthors() throws Exception {
        var expectedGenresCount = dbGenres.size();
        var expectedGenreNames = dbGenres.stream()
                                         .map(Genre::getName)
                                         .toList();

        when(genreService.findAll()).thenReturn(dbGenres);

        this.mvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(view().name("genres"))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("genres", hasSize(expectedGenresCount)))
                .andExpect(content().string(stringContainsInOrder(expectedGenreNames)));
    }
}

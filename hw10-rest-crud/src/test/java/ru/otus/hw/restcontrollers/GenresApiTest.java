package ru.otus.hw.restcontrollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.services.GenreService;
import ru.otus.hw.util.TestDataGenerator;
import java.util.List;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({GenresApi.class, GenreMapper.class})
@DisplayName("API жанров должен:")
class GenresApiTest {

    private List<GenreDto> genres;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GenreMapper gm;

    @MockitoBean
    private GenreService genreService;

    @BeforeEach
    void setUp() {
        genres = gm.genreToDto(TestDataGenerator.getDbGenres());
    }

    @DisplayName("Возвращать массив жанров при GET /api/genres")
    @Test
    void genresrApiTest() throws Exception {

        when(genreService.findAll()).thenReturn(genres);

        this.mvc.perform(get("/api/genres"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(genres)));

        verify(genreService, times(1)).findAll();
    }

    @DisplayName("Возвращать пустой массив при GET /api/genres и отсутствии авторов")
    @Test
    void authorApiEmptyTest() throws Exception {

        when(genreService.findAll()).thenReturn(List.of());

        this.mvc.perform(get("/api/genres"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));

        verify(genreService, times(1)).findAll();
    }
}
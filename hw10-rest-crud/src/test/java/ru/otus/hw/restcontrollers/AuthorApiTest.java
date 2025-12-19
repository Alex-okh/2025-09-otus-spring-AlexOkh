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
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.util.TestDataGenerator;
import java.util.List;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({AuthorsApi.class, AuthorMapper.class})
@DisplayName("API авторов должен:")
class AuthorApiTest {

    @Autowired
    private AuthorMapper am;

    private List<AuthorDto> authorsDto;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        authorsDto = am.authorToDto(TestDataGenerator.getDbAuthors());
    }

    @DisplayName("Возвращать массив авторов при GET /api/authors")
    @Test
    void authorApiTest() throws Exception {

        when(authorService.findAll()).thenReturn(authorsDto);

        this.mvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(authorsDto)));

        verify(authorService, times(1)).findAll();
    }

    @DisplayName("Возвращать пустой массив при GET /api/authors и отсутствии авторов")
    @Test
    void authorApiEmptyTest() throws Exception {

        when(authorService.findAll()).thenReturn(List.of());

        this.mvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));

        verify(authorService, times(1)).findAll();
    }
}
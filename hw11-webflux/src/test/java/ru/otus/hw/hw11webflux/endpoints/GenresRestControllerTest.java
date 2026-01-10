package ru.otus.hw.hw11webflux.endpoints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.hw.hw11webflux.controllers.GenreController;
import ru.otus.hw.hw11webflux.dto.GenreDto;
import ru.otus.hw.hw11webflux.mappers.GenreMapper;
import ru.otus.hw.hw11webflux.service.GenreService;
import ru.otus.hw.hw11webflux.util.TestDataGenerator;
import java.util.List;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = GenreController.class)
@Import(GenreMapper.class)
@DisplayName("API жанров должен:")
class GenresRestControllerTest {

    @Autowired
    private GenreMapper gm;

    private List<GenreDto> genresDtos;

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private GenreService genreService;

    @BeforeEach
    void setUp() {
        genresDtos = gm.toDto(TestDataGenerator.getDbGenres());
    }

    @DisplayName("Возвращать жанры при GET /api/flux/genres")
    @Test
    void genreApiTest() {

        when(genreService.findAll()).thenReturn(Flux.fromIterable(genresDtos));

        webTestClient.get()
                     .uri("/api/flux/genres")
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBodyList(GenreDto.class)
                     .isEqualTo(genresDtos);
        verify(genreService, times(1)).findAll();
    }

    @DisplayName("Возвращать пустой массив при GET /api/authors и отсутствии авторов")
    @Test
    void genreApiEmptyTest() {

        when(genreService.findAll()).thenReturn(Flux.empty());

        webTestClient.get()
                     .uri("/api/flux/genres")
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBodyList(GenreDto.class)
                     .hasSize(0);
        verify(genreService, times(1)).findAll();
    }
}
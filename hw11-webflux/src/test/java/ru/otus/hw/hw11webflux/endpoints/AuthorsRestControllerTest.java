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
import ru.otus.hw.hw11webflux.controllers.AuthorController;
import ru.otus.hw.hw11webflux.dto.AuthorDto;
import ru.otus.hw.hw11webflux.mappers.AuthorMapper;
import ru.otus.hw.hw11webflux.service.AuthorService;
import ru.otus.hw.hw11webflux.util.TestDataGenerator;
import java.util.List;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = AuthorController.class)
@Import(AuthorMapper.class)
@DisplayName("API авторов должен:")
class AuthorsRestControllerTest {

    @Autowired
    private AuthorMapper am;

    private List<AuthorDto> authorsDto;

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        authorsDto = am.toDto(TestDataGenerator.getDbAuthors());
    }

    @DisplayName("Возвращать авторов при GET /api/flux/authors")
    @Test
    void authorApiTest() {

        when(authorService.findAll()).thenReturn(Flux.fromIterable(authorsDto));

        webTestClient.get()
                     .uri("/api/flux/authors")
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBodyList(AuthorDto.class)
                     .isEqualTo(authorsDto);
        verify(authorService, times(1)).findAll();
    }

    @DisplayName("Возвращать пустой массив при GET /api/authors и отсутствии авторов")
    @Test
    void authorApiEmptyTest() {

        when(authorService.findAll()).thenReturn(Flux.empty());

        webTestClient.get()
                     .uri("/api/flux/authors")
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBodyList(AuthorDto.class)
                     .hasSize(0);
        verify(authorService, times(1)).findAll();
    }
}
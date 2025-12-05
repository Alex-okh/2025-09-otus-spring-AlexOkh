package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Genre;
import java.util.List;
import java.util.stream.IntStream;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис жанров должен")
@DataMongoTest
@Import({GenreServiceImpl.class})
@Transactional(propagation = Propagation.NEVER)
class GenresServiceImplTest {
    private final int EXPECTED_GENRES_COUNT = 6;

    @Autowired
    private GenreService genreService;

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7)
                        .boxed()
                        .map(id -> new Genre("g_id_" + id, "Genre_" + id))
                        .toList();
    }

    @DisplayName("Загружать все жанры")
    @Test
    void findAllTest() {
        var actualGenres = genreService.findAll();
        var expectedGenres = getDbGenres();
        assertThat(actualGenres).isNotEmpty()
                                .hasSize(EXPECTED_GENRES_COUNT)
                                .usingRecursiveComparison()
                                .isEqualTo(expectedGenres);

    }
}

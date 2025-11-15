package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с жанрами ")
@DataJpaTest
@Import(JPAGenreRepository.class)
class JPAGenreRepositoryTest {

    private Set<Long> INCORRECT_IDS = Set.of(-1L, 1000L, -1000L);

    @Autowired
    private JPAGenreRepository repo;

    @Autowired
    private TestEntityManager em;

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7)
                        .boxed()
                        .map(id -> new Genre(id, "Genre_" + id))
                        .toList();
    }

    private static List<Set<Genre>> getDbGenresByBatch() {
        var genres = getDbGenres();
        return IntStream.range(0, genres.size() - 1)
                        .mapToObj(i -> Set.copyOf(genres.subList(0, i + 1)))
                        .toList();
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        var actualGenres = repo.findAll();
        var expectedGenres = getDbGenres();

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
    }

    @DisplayName("должен загружать жанр по одному id")
    @ParameterizedTest
    @MethodSource("getDbGenres")
    void shouldReturnCorrectGenreById(Genre expectedGenre) {
        var actualGenres = repo.findAllByIds(Set.of(expectedGenre.getId()));

        assertThat(actualGenres).containsExactlyElementsOf(Set.of(expectedGenre));
    }

    @DisplayName("должен загружать жанры по нескольким id")
    @ParameterizedTest
    @MethodSource("getDbGenresByBatch")
    void shouldReturnCorrectGenresByIdSet(Set<Genre> expectedGenres) {
        var expectedGenreIds = expectedGenres.stream()
                                             .map(Genre::getId)
                                             .collect(Collectors.toSet());
        var actualGenres = repo.findAllByIds(expectedGenreIds);

        assertThat(actualGenres).containsExactlyInAnyOrderElementsOf(expectedGenres);
    }

    @DisplayName("должен возвращать пустой лист по несуществующему ID")
    @ParameterizedTest
    @ValueSource(longs = {-1, 1000, -1000})
    void shouldReturnEmptyGenreListByIncorrectId(long incorrectId) {
        var actualGenre = repo.findAllByIds(Set.of(incorrectId));

        assertThat(actualGenre).isEmpty();
    }

    @DisplayName("должен возвращать непустой лист по смеси существующих и несуществующих ID")
    @ParameterizedTest
    @MethodSource("getDbGenresByBatch")
    void shouldReturnCorrectGenreByPertiallyCorrectIdSet(Set<Genre> expectedGenres) {
        var genreIds = expectedGenres.stream()
                                     .map(Genre::getId)
                                     .collect(Collectors.toSet());
        genreIds.addAll(INCORRECT_IDS);
        var actualGenres = repo.findAllByIds(genreIds);

        assertThat(actualGenres).containsExactlyInAnyOrderElementsOf(expectedGenres);
    }
}

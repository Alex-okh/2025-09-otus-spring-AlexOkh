package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import java.util.List;
import java.util.stream.IntStream;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис авторов должен")
@DataMongoTest
@Import({AuthorServiceImpl.class})
@Transactional(propagation = Propagation.NEVER)
class AuthorServiceImplTest {

    private final int EXPECTED_AUTHORS_COUNT = 3;

    @Autowired
    private AuthorService authorService;

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4)
                        .boxed()
                        .map(id -> new Author("a_id_" + id, "Author_" + id))
                        .toList();
    }

    @DisplayName("Загружать всех авторов")
    @Test
    void findAllTest() {
        var actualAuthors = authorService.findAll();
        var expectedAuthors = getDbAuthors();
        assertThat(actualAuthors).isNotEmpty()
                                 .hasSize(EXPECTED_AUTHORS_COUNT)
                                 .usingRecursiveComparison()
                                 .isEqualTo(expectedAuthors);

    }
}
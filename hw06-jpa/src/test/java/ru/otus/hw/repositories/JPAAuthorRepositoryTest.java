package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("репозиторий для работы с авторами должен:")
@DataJpaTest
@Import(JPAAuthorRepository.class)
public class JPAAuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;

    @DisplayName("загружать список авторов")
    @Test
    void findAllTest() {
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).hasSize(3);
    }

    @DisplayName("загружать автора по ID")
    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3})
    void findByIdTest(long id) {
        var author = authorRepository.findById(id);
        assertThat(author).isNotEmpty()
                          .get()
                          .hasFieldOrPropertyWithValue("id", id)
                          .hasFieldOrPropertyWithValue("fullName", "Author_" + id);
    }

    @DisplayName("загружать пустого автора по несуществующему ID")
    @ParameterizedTest
    @ValueSource(longs = {0, 100, -100})
    void findByWrongIdTest(long id) {
        var author = authorRepository.findById(id);
        assertThat(author).isEmpty();
    }

}

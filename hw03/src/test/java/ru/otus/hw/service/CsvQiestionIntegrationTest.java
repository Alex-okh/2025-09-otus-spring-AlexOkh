package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CsvQiestionIntegrationTest {
    static final String BAD_TEST_FILE_NAME = "test.csv";
    static final String REAL_TEST_FILE_NAME = "questions.csv";

    @Mock
    private TestFileNameProvider fileNameProvider;

    private CsvQuestionDao csvQuestionDao;

    @BeforeEach
    void setUp() {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        csvQuestionDao = new CsvQuestionDao(fileNameProvider, resourceLoader);
    }

    @Test
    @DisplayName("Должен прочитать корректные данные")
    void csvQuestionDao_GoodTest() {
        when(fileNameProvider.getTestFileName()).thenReturn(REAL_TEST_FILE_NAME);

        List<Question> readQuestions = csvQuestionDao.findAll();

        assertThat(readQuestions).hasSize(3);
        assertThat(readQuestions.get(0)
                                .answers()).hasSize(3);
        assertThat(readQuestions.get(1)
                                .answers()).hasSize(3);
        assertThat(readQuestions.get(0)
                                .text()).isEqualTo("Is there life on Mars?");
        assertThat(readQuestions.get(1)
                                .answers()
                                .getFirst()
                                .text()).isEqualTo(
                "ClassLoader#geResourceAsStream or ClassPathResource#getInputStream");
        assertTrue(readQuestions.get(1)
                                .answers()
                                .getFirst()
                                .isCorrect());
    }

    @Test
    @DisplayName("Должен выбросить исключение при ошибке ввода-вывода")
    void csvQuestionDao_BadTest() {
        when(fileNameProvider.getTestFileName()).thenReturn(BAD_TEST_FILE_NAME);

        assertThatThrownBy(() -> csvQuestionDao.findAll()).isInstanceOf(QuestionReadException.class);

    }

}





package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CsvQuestionDaoTest {
    static final String TEST_FILE_NAME = "test.csv";
    static final String TEST_FILE_CONTENT = """
            первая строка пропускается
            Question1?;correct answer%true|incorrect1%false|incorrect2%false
            Question2?;incorrect1%false|correct%true|incorrect2%false|incorrect3%false
            """;

    @Mock
    Resource resource;
    @Mock
    private TestFileNameProvider fileNameProvider;
    @Mock
    private ResourceLoader resourceLoader;
    @InjectMocks
    private CsvQuestionDao csvQuestionDao;

    @Test
    @DisplayName("Должен прочитать корректные данные")
    void csvQuestionDao_GoodTest() throws IOException {
        when(fileNameProvider.getTestFileName()).thenReturn(TEST_FILE_NAME);
        when(resourceLoader.getResource(TEST_FILE_NAME)).thenReturn(resource);
        when(resource.getInputStream()).thenReturn(new ByteArrayInputStream(TEST_FILE_CONTENT.getBytes()));

        List<Question> readQuestions = csvQuestionDao.findAll();

        assertThat(readQuestions).hasSize(2);
        assertThat(readQuestions.get(0)
                                .answers()).hasSize(3);
        assertThat(readQuestions.get(1)
                                .answers()).hasSize(4);
        assertThat(readQuestions.get(0)
                                .text()).isEqualTo("Question1?");
        assertThat(readQuestions.get(1)
                                .answers()
                                .getFirst()
                                .text()).isEqualTo("incorrect1");
    }

    @Test
    @DisplayName("Должен выбросить исключение при ошибке ввода-вывода")
    void csvQuestionDao_BadTest() throws IOException {
        when(fileNameProvider.getTestFileName()).thenReturn(TEST_FILE_NAME);
        when(resourceLoader.getResource(TEST_FILE_NAME)).thenReturn(resource);
        when(resource.getInputStream()).thenThrow(new IOException());

        assertThatThrownBy(() -> csvQuestionDao.findAll()).isInstanceOf(QuestionReadException.class);

    }
}





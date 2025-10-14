package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestServiceTest {
    private final String TEST_FIRSTNAME = "<Student firstname>";
    private final String TEST_LASTNAME = "<Student lastname>";


    @Mock
    private IOService ioService;

    @Mock
    private QuestionDao questionDao;

    @InjectMocks
    private TestServiceImpl testService;

    @Test
    @DisplayName("Должен считать результат")
    void testRunTest() {
        List<Question> testQuestions = List.of(
                new Question("Questiion 1", List.of(
                        new Answer("Answer1", true),
                        new Answer("Answer2", false))),
                new Question("Questiion 2", List.of(
                        new Answer("Answer1-2", false),
                        new Answer("Answer2-2", true))));
        when(questionDao.findAll()).thenReturn(testQuestions);
        when(ioService.readIntForRange(anyInt(),anyInt(),anyString())).thenReturn(1);

        var testResult = testService.executeTestFor(new Student(TEST_FIRSTNAME, TEST_LASTNAME));

        assertThat(testResult.getRightAnswersCount()).isEqualTo(1);
        assertThat(testResult.getAnsweredQuestions().size()).isEqualTo(2);
        assertThat(testResult.getStudent().getFullName()).isEqualTo("<Student firstname> <Student lastname>");

    }
}

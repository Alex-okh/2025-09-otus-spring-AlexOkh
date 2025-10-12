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
import java.util.List;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestServiceTest {

    @Mock
    private IOService ioService;

    @Mock
    private QuestionDao questionDao;

    @Mock
    private InputService inputService;

    @InjectMocks
    private TestServiceImpl testService;

    @Test
    @DisplayName("Должен выводить вопросы и считать результат")
    void testRunTest() {
        List<Question> testQuestions = List.of(
                new Question("Questiion 1", List.of(
                        new Answer("Answer1", true),
                        new Answer("Answer2", false))),
                new Question("Questiion 2", List.of(
                        new Answer("Answer1-2", false),
                        new Answer("Answer2-2", true))));
        when(questionDao.findAll()).thenReturn(testQuestions);
        when(inputService.getInt()).thenReturn(1);

        testService.executeTest();
        verify(inputService, times(2)).getInt();
        verify(ioService, times(10)).printLine(anyString());
        verify(ioService, times(5)).printFormattedLine(anyString());
        verify(ioService).printLine("Your score is 1 of 2");
    }
}

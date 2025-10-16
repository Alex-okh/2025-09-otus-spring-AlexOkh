package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestRunnerServiceTest {
    private final String TEST_FIRSTNAME = "<Student firstname>";
    private final String TEST_LASTNAME = "<Student lastname>";

    @Mock
    private TestService testService;

    @Mock
    private StudentService studentService;

    @Mock
    private ResultService resultService;

    @InjectMocks
    private TestRunnerServiceImpl testRunnerService;

    @Test
    @DisplayName("Должен запускать executeTest с полученным студентом")
    void testRun() {
        when(studentService.determineCurrentStudent()).thenReturn(new Student(TEST_FIRSTNAME, TEST_LASTNAME));
        testRunnerService.run();
        verify(testService).executeTestFor(studentService.determineCurrentStudent());
    }

    @Test
    @DisplayName("Должен вызывать показ результатов")
    void testShowResults() {
        var testStudent = new Student(TEST_FIRSTNAME, TEST_LASTNAME);
        when(studentService.determineCurrentStudent()).thenReturn(testStudent);
        when(testService.executeTestFor(testStudent)).thenReturn(new TestResult(testStudent));
        testRunnerService.run();
        verify(resultService).showResult(isA(TestResult.class));
    }

}

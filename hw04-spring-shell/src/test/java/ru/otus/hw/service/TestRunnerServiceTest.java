package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TestRunnerServiceImpl.class)
class TestRunnerServiceTest {
    private final String TEST_FIRSTNAME = "<Student firstname>";
    private final String TEST_LASTNAME = "<Student lastname>";

    @MockitoBean
    private TestService testService;

    @MockitoBean
    private RegisterContext registerContext;

    @MockitoBean
    private ResultService resultService;

    @Autowired
    private TestRunnerServiceImpl testRunnerService;

    @Test
    @DisplayName("Должен запускать executeTest с полученным студентом")
    void testRun() {
        when(registerContext.getStudent()).thenReturn(new Student(TEST_FIRSTNAME, TEST_LASTNAME));
        testRunnerService.run();
        verify(testService).executeTestFor(registerContext.getStudent());
    }

    @Test
    @DisplayName("Должен вызывать показ результатов")
    void testShowResults() {
        var testStudent = new Student(TEST_FIRSTNAME, TEST_LASTNAME);
        when(registerContext.getStudent()).thenReturn(testStudent);
        when(testService.executeTestFor(testStudent)).thenReturn(new TestResult(testStudent));
        testRunnerService.run();
        verify(resultService).showResult(isA(TestResult.class));
    }

}

package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TestRunnerServiceTest {
    @Mock
    private TestService testService;

    @InjectMocks
    private TestRunnerServiceImpl testRunnerService;

    @Test
    @DisplayName("Должен запускать executeTest")
    void testRun() {
        testRunnerService.run();
        verify(testService).executeTest();
    }

}

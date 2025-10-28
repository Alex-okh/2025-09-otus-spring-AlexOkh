package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    private final RegisterContext registerContext;

    private final ResultService resultService;

    @Override
    public void run() {
        var student = registerContext.getStudent();
        var testResult = testService.executeTestFor(student);
        resultService.showResult(testResult);
    }
}

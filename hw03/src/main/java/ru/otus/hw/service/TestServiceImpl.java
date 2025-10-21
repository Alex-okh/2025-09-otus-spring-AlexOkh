package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            var isAnswerValid = askQuestion(question); // Задать вопрос, получить ответ
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private boolean askQuestion(Question question) {
        ioService.printLine("");
        ioService.printFormattedLine(question.text());
        return processAnswers(question);

    }

    private boolean processAnswers(Question question) {
        int correctAnswerNumber = 0;
        for (int i = 0; i < question.answers()
                                    .size(); i++) {
            var answer = question.answers()
                                 .get(i);
            ioService.printLine((i + 1) + ". " + answer.text());
            if (answer.isCorrect()) {
                correctAnswerNumber = i + 1;
            }
        }
        ioService.printLine("");
        ioService.printFormattedLineLocalized("TestService.answer.numberPrompt");
        return correctAnswerNumber == getAnswerNumber(1, question.answers()
                                                                 .size());
    }

    private int getAnswerNumber(int lowerBound, int upperBound) {
        return ioService.readIntForRangeLocalized(lowerBound, upperBound, "TestService.answer.wrongOption");
    }

}

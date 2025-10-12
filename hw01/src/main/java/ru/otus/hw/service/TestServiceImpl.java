package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final InputService inputService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        var questions = questionDao.findAll();
        int score = 0;
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below:");
        for (var q : questions) {
            if (askQuestion(q)) {
                score++;
            }
        }
        ioService.printLine("Your score is " + score + " of " + questions.size());
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
            var answer = question.answers().get(i);
            ioService.printLine((i + 1) + ". " + answer.text());
            if (answer.isCorrect()) {
                correctAnswerNumber = i + 1;
            }
        }
        ioService.printLine("");
        ioService.printFormattedLine("Enter answer number:");
        return correctAnswerNumber == getAnswerNumber();
    }

    private int getAnswerNumber() {
       return inputService.getInt();
    }
}

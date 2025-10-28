package ru.otus.hw.runner;

import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.CommandAvailability;
import ru.otus.hw.service.LocalizedMessagesService;
import ru.otus.hw.service.RegisterContext;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestRunnerService;

@Command(group = "Test application commands")
public class AppRunner {
    private final TestRunnerService testRunnerService;

    private final LocalizedMessagesService localizedMessagesService;

    private final StudentService studentService;

    private final RegisterContext registerContext;

    public AppRunner(TestRunnerService testRunnerService, LocalizedMessagesService localizedMessagesService,
                     StudentService studentService, RegisterContext registerContext) {
        this.testRunnerService = testRunnerService;
        this.localizedMessagesService = localizedMessagesService;
        this.studentService = studentService;
        this.registerContext = registerContext;
    }

    @Command(description = "Perform test", command = "t", alias = "test")
    @CommandAvailability(provider = "shellAvailabilityProvider")
    public String runTest() {
        testRunnerService.run();
        return localizedMessagesService.getMessage("ShellCommands.completed");
    }

    @Command(description = "Register", command = "r", alias = "register")
    public String registerStudent() {
        registerContext.register(studentService.determineCurrentStudent());
        return localizedMessagesService.getMessage("ShellCommands.registration");
    }

    @Command(description = "Information", command = "i", alias = "info")
    public String showInfo() {
        return constructInfoString();
    }

    private String constructInfoString() {
        String header = localizedMessagesService.getMessage(
                "ShellCommands.infoHeader");
        String studentHeader = localizedMessagesService.getMessage(
                "ShellCommands.studentHeader");
        String student = registerContext.isStudentRegistered()
                ? registerContext.getStudent().getFullName()
                : localizedMessagesService.getMessage(
                "ShellCommands.infoNobodyRegistered");
        return """
                %s
                %s %s""".formatted(header, studentHeader, student);
    }
}

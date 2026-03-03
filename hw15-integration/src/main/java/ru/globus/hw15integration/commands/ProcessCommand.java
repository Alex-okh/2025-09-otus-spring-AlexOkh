package ru.globus.hw15integration.commands;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.command.CommandContext;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.globus.hw15integration.gateway.ProcessingGateway;

@RequiredArgsConstructor
@ShellComponent
@Slf4j
public class ProcessCommand {
    private final ProcessingGateway processingGateway;

    @SuppressWarnings("unused")
    @ShellMethod(value = "Process files",
                 key = "do")
    public String processFiles(@ShellOption() String directory, CommandContext ctx) {
        ctx.getTerminal()
           .writer()
           .println("Processing files in directory %s".formatted(directory));
        var processedFiles = processingGateway.process(directory);
        if (processedFiles.isEmpty()) {
            return "No image files found.";
        }
        return "Completed. Processed %d files total.".formatted(processedFiles.size());
    }
}

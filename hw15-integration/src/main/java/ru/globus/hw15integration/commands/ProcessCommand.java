package ru.globus.hw15integration.commands;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.shell.command.CommandContext;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@RequiredArgsConstructor
@ShellComponent
@Slf4j
public class ProcessCommand {
    private final MessageChannel processDirectoryChannel;

    @SuppressWarnings("unused")
    @ShellMethod(value = "Process files",
                 key = "do")
    public String processFiles(@ShellOption() String directory, CommandContext ctx) {
        ctx.getTerminal()
           .writer()
           .println("Processing files in directory %s".formatted(directory));
        processDirectoryChannel.send(MessageBuilder.withPayload(directory)
                                                   .build());
        return "Completed";
    }
}

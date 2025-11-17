package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentsService;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentsCommands {

    private final CommentsService commentsService;

    private final CommentConverter converter;

    @ShellMethod(value = "Find comment by id", key = "ci")
    public String findCommentById(long id) {
        return commentsService.findById(id)
                              .map(converter::commmentToString)
                              .orElse("Comment with ID %d not found".formatted(id));
    }

    @ShellMethod(value = "Find all comments for book id", key = "cbi")
    public String findAllCommentsByBookId(long bookId) {
        return commentsService.findAllByBookId(bookId)
                              .stream()
                              .map(converter::commmentToString)
                              .collect(Collectors.joining("," + System.lineSeparator()));
    }

}

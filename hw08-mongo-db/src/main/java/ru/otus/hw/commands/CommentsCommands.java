package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentsService;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentsCommands {

    private final CommentsService commentsService;

    private final BookService bookService;

    private final CommentConverter converter;

    @ShellMethod(value = "Find all comments for book id", key = "cbi")
    public String findAllCommentsByBookId(String bookId) {
        return "Comments to book: " + bookService.findById(bookId).get().getTitle() + "\n" +commentsService.findAllByBookId(bookId)
                              .stream()
                              .map(converter::commmentToString)
                              .collect(Collectors.joining("," + System.lineSeparator()));
    }

}
